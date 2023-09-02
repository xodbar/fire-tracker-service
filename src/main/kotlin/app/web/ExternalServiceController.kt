package app.web

import app.core.meteo.OpenMeteoService
import app.core.nasa.NasaFirmsResponse
import app.core.nasa.NasaFirmsService
import app.core.nasa.SupportedCountry
import app.core.utils.csvToJson
import app.core.utils.getCurrentAlmatyLocalDateTime
import app.useCase.AnalyseElement
import app.useCase.AnalyseRequest
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping(value = ["/external-service"])
class ExternalServiceController(
  private val nasaFirmsService: NasaFirmsService,
  private val openMeteoService: OpenMeteoService
) {

  @GetMapping("/nasa-firms")
  @ResponseBody
  fun getNasaFirmsData(
    @RequestParam country: SupportedCountry,
    @RequestParam toDate: LocalDate,
    @RequestParam range: Int
  ) = nasaFirmsService.getFirmsData(
    country = SupportedCountry.KAZAKHSTAN,
    toDate = getCurrentAlmatyLocalDateTime().toLocalDate(),
    range = 7
  )

  @GetMapping("/open-meteo")
  @ResponseBody
  fun getOpenMeteo(
    @RequestParam latitude: Double,
    @RequestParam longitude: Double,
    @RequestParam pastDays: Int
  ) = openMeteoService.getForecastData(
    latitude = latitude,
    longitude = longitude,
    pastDays = pastDays
  )

  @GetMapping("/build-request")
  @ResponseBody
  fun buildMlRequest(
    @RequestParam country: SupportedCountry,
    @RequestParam toDate: LocalDate,
    @RequestParam range: Int
  ): AnalyseRequest {
    val firmsCsvData = nasaFirmsService.getFirmsData(
      country = SupportedCountry.KAZAKHSTAN,
      toDate = getCurrentAlmatyLocalDateTime().minusDays(1).toLocalDate(),
      range = 7
    )

    val firmsListJson = "{ \"list\": ${csvToJson(firmsCsvData)} }"
    val firmsData = jacksonObjectMapper().readValue(firmsListJson, NasaFirmsResponse::class.java)

    val analyseElements = firmsData.list.map { nasaFirmsElement ->
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

    return AnalyseRequest(analyseElements)
  }
}
