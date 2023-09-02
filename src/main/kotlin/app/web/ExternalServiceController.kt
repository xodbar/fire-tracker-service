package app.web

import app.core.nasa.NasaFirmsService
import app.core.nasa.SupportedCountry
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping(value = ["/external-service"])
class ExternalServiceController(
    private val nasaFirmsService: NasaFirmsService
) {

    @GetMapping("/nasa-firms")
    @ResponseBody
    fun getNasaFirmsData(
        @RequestParam country: SupportedCountry,
        @RequestParam toDate: LocalDate,
        @RequestParam range: Int
    ) = nasaFirmsService.getFirmsData(
        country = country,
        toDate = toDate,
        range = 7
    )
}
