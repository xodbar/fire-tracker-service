package app.web

import app.core.prometheus.PrometheusService
import app.core.utils.getCurrentAlmatyLocalDateTime
import app.useCase.AnalyzeResponseElement
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/health")
class HealthController(
  private val prometheusService: PrometheusService
) {

  @GetMapping
  @ResponseBody
  fun health() = getCurrentAlmatyLocalDateTime()

  @PostMapping("/put-mock-data")
  @ResponseBody
  fun test(
    @RequestParam latitude: Double,
    @RequestParam longitude: Double,
    @RequestParam fireDanger: Int,
    @RequestParam fireRadius: Int
  ) {
    prometheusService.putFireDangerData(
      AnalyzeResponseElement(latitude, longitude, fireDanger, fireRadius)
    )
  }
}
