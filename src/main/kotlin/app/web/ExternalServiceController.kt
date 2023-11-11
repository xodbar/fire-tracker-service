package app.web

import app.core.nasa.NasaFirmsService
import app.core.nasa.SupportedCountry
import app.core.weather.OpenWeatherService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping(value = ["/external-service"])
class ExternalServiceController(
    private val nasaFirmsService: NasaFirmsService,
    private val openWeatherService: OpenWeatherService
) {

    @Operation(summary = "NASA FIRMS Data", description = "Get NASA Firms data")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/nasa-firms")
    @ResponseBody
    fun getNasaFirmsData(
        @RequestParam country: SupportedCountry,
        @RequestParam toDate: LocalDate,
        @RequestParam range: Int
    ) = nasaFirmsService.getFirmsData(
        country = country,
        toDate = toDate,
        range = range
    )

    @Operation(summary = "Open Weather API Data", description = "Get Open Weather API data")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/open-weather")
    @ResponseBody
    fun getOpenWeatherData(
        @RequestParam latitude: Double,
        @RequestParam longitude: Double
    ) = openWeatherService.getFireRiskRate(
        latitude = latitude,
        longitude = longitude
    )
}
