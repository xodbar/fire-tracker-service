package app.core.meteo

import app.core.externalServiceRequest.ExternalServiceRequestManager
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.Serializable
import java.time.LocalDateTime

@Service
class OpenMeteoService(
	private val externalServiceRequestManager: ExternalServiceRequestManager,
	@Qualifier("defaultRestTemplate") private val restTemplate: RestTemplate,
	@Value("\${app.services.open-meteo.url}") private val openMeteoUrl: String
) {

	fun getForecastData(latitude: Double, longitude: Double, pastDays: Int): ForecastResponse {
		val url = getForecastUrl(latitude, longitude, pastDays)
		val rawResponse = restTemplate.getForEntity(
			url,
			String::class.java
		).body ?: throw RuntimeException("Failed to get OpenMeteo data")

		externalServiceRequestManager.saveRequest(url, HttpMethod.GET.name(), "", rawResponse)
		return jacksonObjectMapper().readValue(rawResponse, ForecastResponse::class.java)
	}

	private fun getForecastUrl(latitude: Double, longitude: Double, pastDays: Int) =
		"$openMeteoUrl/forecast?latitude=$latitude&longitude=$longitude&past_days=$pastDays&hourly=temperature_2m,relativehumidity_2m,windspeed_10m"
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class ForecastResponse(

	@JsonProperty("latitude")
	val latitude: Double,

	@JsonProperty("longitude")
	val longitude: Double,

	@JsonProperty("generationtime_ms")
	val generationTimeMs: Double,

	@JsonProperty("utc_offset_seconds")
	val urcOffsetSeconds: Int,

	@JsonProperty("timezone")
	val timezone: String,

	@JsonProperty("timezone_abbreviation")
	val timezoneAbbreviation: String,

	@JsonProperty("elevation")
	val elevation: Double,

	@JsonProperty("hourly_units")
	val hourlyUnits: HourlyUnits,

	@JsonProperty("hourly")
	val hourly: HourlyData
) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class HourlyUnits(

	@JsonProperty("time")
	val time: String,

	@JsonProperty("temperature_2m")
	val temperature: String,

	@JsonProperty("relativehumidity_2m")
	val relativeHumidity2m: String,

	@JsonProperty("windspeed_10m")
	val windSpeed10m: String
) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class HourlyData(

	@JsonProperty("time")
	val time: List<LocalDateTime?>,

	@JsonProperty("temperature_2m")
	val temperature2m: List<Double?>,

	@JsonProperty("relativehumidity_2m")
	val relativeHumidity2m: List<Int?>,

	@JsonProperty("windspeed_10m")
	val windSpeed10m: List<Double?>
) : Serializable
