package app.useCase

import app.core.kafka.KafkaMessageProducer
import app.core.kafka.notifyMembers.NotifyConsumersInput
import app.core.location.SensitiveLocationService
import app.core.nasa.NasaFirmsResponse
import app.core.nasa.NasaFirmsService
import app.core.nasa.SupportedCountry
import app.core.prometheus.PrometheusService
import app.core.utils.csvToJson
import app.core.utils.getCurrentAlmatyLocalDateTime
import app.core.weather.OpenWeatherService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UpdateRealTimeDataUseCase(
    private val firmsService: NasaFirmsService,
    private val prometheusService: PrometheusService,
    private val openWeatherService: OpenWeatherService,
    private val sensitiveLocationService: SensitiveLocationService,
    private val kafkaMessageProducer: KafkaMessageProducer,
    @Value("\${app.topics.notify-consumers}") private val notifyConsumersTopic: String,
    @Value("\${app.constant.risk-rate}") private val maximumRiskRate: Int
) {

    @Transactional
    fun handle() {
        val firmsCsvData = firmsService.getFirmsData(
            country = SupportedCountry.UNITED_STATES,
            toDate = getCurrentAlmatyLocalDateTime().minusDays(1).toLocalDate(),
            range = 1
        )

        val firmsListJson = "{ \"list\": ${csvToJson(firmsCsvData)} }"
        val firmsData = jacksonObjectMapper().readValue(firmsListJson, NasaFirmsResponse::class.java)

        firmsData.list.shuffled().take(100).map { firmsElement ->
            val openWeatherData = openWeatherService.getFireRiskRate( // just to ensure risk rate
                latitude = firmsElement.latitude,
                longitude = firmsElement.longitude
            )

            val riskRate = openWeatherData.getAverageRiskRate()
            if (riskRate >= maximumRiskRate) {
                sensitiveLocationService.save(firmsElement.latitude, firmsElement.longitude, riskRate)
                prometheusService.putRealTimeData(firmsElement.latitude, firmsElement.longitude, riskRate)
                notifyConsumers(firmsElement.latitude, firmsElement.longitude, riskRate)
            }
        }
    }

    private fun notifyConsumers(latitude: Double, longitude: Double, riskRate: Int) = kafkaMessageProducer.sendMessage(
        topic = notifyConsumersTopic,
        message = jacksonObjectMapper().writeValueAsString(
            NotifyConsumersInput(
                latitude = latitude,
                longitude = longitude,
                riskRate = riskRate
            )
        )
    )
}
