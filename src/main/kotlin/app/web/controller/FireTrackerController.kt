package app.web.controller

import app.core.nasa.NasaFirmsResponse
import app.core.nasa.NasaFirmsService
import app.core.nasa.SupportedCountry
import app.core.utils.csvToJson
import app.core.utils.getCurrentAlmatyLocalDateTime
import app.core.weather.OpenWeatherService
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.*
import java.io.Serializable

@RestController
@RequestMapping("/fire-tracker")
class FireTrackerController(
    private val nasaFirmsService: NasaFirmsService,
    private val openWeatherService: OpenWeatherService
) {

    @Operation(summary = "Real Time Data", description = "Get real-time fire data")
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/real-time-data")
    @ResponseBody
    fun getRealTimeData(
        @RequestParam country: SupportedCountry
    ): List<RealTimeDataResponse> {
        val firmsCsvData = nasaFirmsService.getFirmsData(
            country = country,
            toDate = getCurrentAlmatyLocalDateTime().minusDays(1).toLocalDate(),
            range = 1
        )

        val firmsListJson = "{ \"list\": ${csvToJson(firmsCsvData)} }"
        val firmsData = jacksonObjectMapper().readValue(firmsListJson, NasaFirmsResponse::class.java)

        return firmsData.list.shuffled().take(100).map { firmsElement ->
            val openWeatherData = openWeatherService.getFireRiskRate( // just to ensure risk rate
                latitude = firmsElement.latitude,
                longitude = firmsElement.longitude
            )

            val riskRate = openWeatherData.getAverageRiskRate()
            RealTimeDataResponse(
                latitude = firmsElement.latitude,
                longitude = firmsElement.longitude,
                riskRate = riskRate
            )
        }
    }
}

data class RealTimeDataResponse(
    val latitude: Double,
    val longitude: Double,
    val riskRate: Int
) : Serializable
