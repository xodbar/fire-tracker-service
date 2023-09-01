package app.web

import app.core.meteo.OpenMeteoService
import app.core.nasa.NasaFirmsService
import app.core.nasa.SupportedCountry
import app.core.utils.getCurrentAlmatyLocalDateTime
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
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
}
