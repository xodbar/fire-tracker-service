package app.useCase

import app.core.ai.AiPredictionService
import app.core.meteo.ForecastResponse
import app.core.meteo.OpenMeteoService
import app.core.nasa.NasaFirmsResponse
import app.core.nasa.NasaFirmsService
import app.core.nasa.SupportedCountry
import app.core.prometheus.PrometheusService
import app.core.utils.csvToJson
import app.core.utils.getCurrentAlmatyLocalDateTime
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import java.time.LocalDate

@Component
class UpdateFirmsDataUseCase(
	private val firmsService: NasaFirmsService,
	private val openMeteoService: OpenMeteoService,
	private val aiPredictionService: AiPredictionService,
	private val prometheusService: PrometheusService
) {

	@Transactional
	fun handle() {
		val firmsCsvData = firmsService.getFirmsData(
			country = SupportedCountry.KAZAKHSTAN,
			toDate = getCurrentAlmatyLocalDateTime().minusDays(1).toLocalDate(),
			range = 7
		)

		val firmsListJson = "{ list: ${csvToJson(firmsCsvData)} }"
		val firmsData = jacksonObjectMapper().readValue(firmsListJson, NasaFirmsResponse::class.java)

		val analyseElements = mutableListOf<AnalyseElement>()
		firmsData.list.map { nasaFirmsElement ->
			val openMeteoData = openMeteoService.getForecastData(nasaFirmsElement.latitude, nasaFirmsElement.longitude, 1)
			AnalyseElement(
				countryId = nasaFirmsElement.countryId,
				latitude = nasaFirmsElement.latitude,
				longitude = nasaFirmsElement.longitude,
				brightTi4 = nasaFirmsElement.brightTi4,
				scan = nasaFirmsElement.scan,
				track = nasaFirmsElement.track,
				acqDate = nasaFirmsElement.acqDate,
				acqTime = nasaFirmsElement.acqTime,
				satellite = nasaFirmsElement.satellite,
				instrument = nasaFirmsElement.instrument,
				confidence = nasaFirmsElement.confidence,
				version = nasaFirmsElement.version,
				brightTi5 = nasaFirmsElement.brightTi5,
				frp = nasaFirmsElement.frp,
				dayNight = nasaFirmsElement.dayNight,
				weatherForecast = openMeteoData
			)
		}

		aiPredictionService.analyzeFireData(AnalyseRequest(analyseElements)).data.forEach { analyseElement ->
			prometheusService.putFireDangerData(analyseElement)
		}
	}
}

data class AnalyseRequest(
	val data: List<AnalyseElement>
) : Serializable

data class AnalyseElement(
	val countryId: Long?,
	val latitude: Double?,
	val longitude: Double?,
	val brightTi4: Double?,
	val scan: Double?,
	val track: Double?,
	val acqDate: LocalDate?,
	val acqTime: Int?,
	val satellite: String?,
	val instrument: String?,
	val confidence: String?,
	val version: String?,
	val brightTi5: Double?,
	val frp: Double?,
	val dayNight: String?,
	val weatherForecast: ForecastResponse
) : Serializable

data class AnalyzeResponse(
	val data: List<AnalyzeResponseElement>
) : Serializable

data class AnalyzeResponseElement(
	val latitude: Double?,
	val longitude: Double?,
	val fireDangerRate: Int,
	val fireDangerRadius: Int
) : Serializable
