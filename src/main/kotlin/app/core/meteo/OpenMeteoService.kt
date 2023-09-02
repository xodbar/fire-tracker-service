package app.core.meteo

import app.core.externalServiceRequest.ExternalServiceRequestManager
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class OpenMeteoService(
  private val externalServiceRequestManager: ExternalServiceRequestManager,
  @Qualifier("defaultRestTemplate") private val restTemplate: RestTemplate,
  @Value("\${app.services.open-meteo.url}") private val openMeteoUrl: String
) {

  fun getForecastData(latitude: Double, longitude: Double, pastDays: Int): WeatherData {
    val url = getForecastUrl(latitude, longitude, pastDays)
    val rawResponse = restTemplate.getForEntity(
      url,
      String::class.java
    ).body ?: throw RuntimeException("Failed to get OpenMeteo data")

//    externalServiceRequestManager.saveRequest(url, HttpMethod.GET.name(), "", rawResponse)
    return jacksonObjectMapper().readValue(rawResponse, WeatherData::class.java)
  }

  private fun getForecastUrl(latitude: Double, longitude: Double, pastDays: Int) =
    "$openMeteoUrl/forecast?latitude=$latitude&longitude=$longitude&hourly=windspeed_10m&daily=weathercode,temperature_2m_max,temperature_2m_min,apparent_temperature_max,apparent_temperature_min,sunrise,sunset,uv_index_max,uv_index_clear_sky_max,precipitation_sum,rain_sum,showers_sum,snowfall_sum,precipitation_hours,precipitation_probability_max,windspeed_10m_max,windgusts_10m_max,winddirection_10m_dominant,shortwave_radiation_sum,et0_fao_evapotranspiration&timeformat=unixtime&timezone=auto&past_days=$pastDays"
}

data class WeatherData(
  @JsonProperty("latitude") val latitude: Double,
  @JsonProperty("longitude") val longitude: Double,
  @JsonProperty("generationtime_ms") val generationTimeMs: Double,
  @JsonProperty("utc_offset_seconds") val utcOffsetSeconds: Int,
  @JsonProperty("timezone") val timezone: String,
  @JsonProperty("timezone_abbreviation") val timezoneAbbreviation: String,
  @JsonProperty("elevation") val elevation: Double,
  @JsonProperty("hourly_units") val hourlyUnits: HourlyUnits,
  @JsonProperty("hourly") val hourly: HourlyData,
  @JsonProperty("daily_units") val dailyUnits: DailyUnits,
  @JsonProperty("daily") val daily: DailyData
)

data class HourlyUnits(
  @JsonProperty("time") val time: String,
  @JsonProperty("windspeed_10m") val windspeed10m: String
)

data class HourlyData(
  @JsonProperty("time") val time: List<Long>,
  @JsonProperty("windspeed_10m") val windspeed10m: List<Double>
)

data class DailyUnits(
  @JsonProperty("time") val time: String,
  @JsonProperty("weathercode") val weathercode: String,
  @JsonProperty("temperature_2m_max") val temperature2mMax: String,
  @JsonProperty("temperature_2m_min") val temperature2mMin: String,
  @JsonProperty("apparent_temperature_max") val apparentTemperatureMax: String,
  @JsonProperty("apparent_temperature_min") val apparentTemperatureMin: String,
  @JsonProperty("sunrise") val sunrise: String,
  @JsonProperty("sunset") val sunset: String,
  @JsonProperty("uv_index_max") val uvIndexMax: String,
  @JsonProperty("uv_index_clear_sky_max") val uvIndexClearSkyMax: String,
  @JsonProperty("precipitation_sum") val precipitationSum: String,
  @JsonProperty("rain_sum") val rainSum: String,
  @JsonProperty("showers_sum") val showersSum: String,
  @JsonProperty("snowfall_sum") val snowfallSum: String,
  @JsonProperty("precipitation_hours") val precipitationHours: String,
  @JsonProperty("precipitation_probability_max") val precipitationProbabilityMax: String,
  @JsonProperty("windspeed_10m_max") val windspeed10mMax: String,
  @JsonProperty("windgusts_10m_max") val windgusts10mMax: String,
  @JsonProperty("winddirection_10m_dominant") val winddirection10mDominant: String,
  @JsonProperty("shortwave_radiation_sum") val shortwaveRadiationSum: String,
  @JsonProperty("et0_fao_evapotranspiration") val et0FaoEvapotranspiration: String
)

data class DailyData(
  @JsonProperty("time") val time: List<Long>,
  @JsonProperty("weathercode") val weathercode: List<Int>,
  @JsonProperty("temperature_2m_max") val temperature2mMax: List<Double>,
  @JsonProperty("temperature_2m_min") val temperature2mMin: List<Double>,
  @JsonProperty("apparent_temperature_max") val apparentTemperatureMax: List<Double>,
  @JsonProperty("apparent_temperature_min") val apparentTemperatureMin: List<Double>,
  @JsonProperty("sunrise") val sunrise: List<Long>,
  @JsonProperty("sunset") val sunset: List<Long>,
  @JsonProperty("uv_index_max") val uvIndexMax: List<String>,
  @JsonProperty("uv_index_clear_sky_max") val uvIndexClearSkyMax: List<String>,
  @JsonProperty("precipitation_sum") val precipitationSum: List<String>,
  @JsonProperty("rain_sum") val rainSum: List<String>,
  @JsonProperty("showers_sum") val showersSum: List<String>,
  @JsonProperty("snowfall_sum") val snowfallSum: List<String>,
  @JsonProperty("precipitation_hours") val precipitationHours: List<Double>,
  @JsonProperty("precipitation_probability_max") val precipitationProbabilityMax: List<Int>,
  @JsonProperty("windspeed_10m_max") val windspeed10mMax: List<Double>,
  @JsonProperty("windgusts_10m_max") val windgusts10mMax: List<Double>,
  @JsonProperty("winddirection_10m_dominant") val winddirection10mDominant: List<Int>,
  @JsonProperty("shortwave_radiation_sum") val shortwaveRadiationSum: List<Double>,
  @JsonProperty("et0_fao_evapotranspiration") val et0FaoEvapotranspiration: List<Double>
)
