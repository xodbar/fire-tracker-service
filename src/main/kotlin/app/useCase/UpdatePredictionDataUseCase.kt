package app.useCase

import app.core.location.SensitiveLocationService
import app.core.prometheus.PrometheusService
import app.core.weather.OpenWeatherService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class UpdatePredictionDataUseCase(
    private val prometheusService: PrometheusService,
    private val openWeatherService: OpenWeatherService,
    private val sensitiveLocationService: SensitiveLocationService
) {

    @Transactional
    fun handle() {
        sensitiveLocationService.getAll().forEach { sensitiveLocation ->
            val openWeatherData = openWeatherService.getFireRiskRate( // just to ensure risk rate
                latitude = sensitiveLocation.latitude,
                longitude = sensitiveLocation.longitude
            )

            val riskRate = openWeatherData.getAverageRiskRate()
            sensitiveLocationService.save(sensitiveLocation.latitude, sensitiveLocation.longitude, riskRate)
            prometheusService.putPredictionData(sensitiveLocation.latitude, sensitiveLocation.longitude, riskRate)
        }
    }
}
