package app.web

import app.core.utils.getCurrentAlmatyLocalDateTime
import app.useCase.UpdatePredictionDataUseCase
import app.useCase.UpdateRealTimeDataUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/health")
class HealthController(
    private val updatePredictionDataUseCase: UpdatePredictionDataUseCase,
    private val updateRealTimeDataUseCase: UpdateRealTimeDataUseCase
) {

    @GetMapping
    @ResponseBody
    fun health() = getCurrentAlmatyLocalDateTime()

    @GetMapping("/test")
    @ResponseBody
    fun test() {
        updateRealTimeDataUseCase.handle()
        updatePredictionDataUseCase.handle()
    }
}
