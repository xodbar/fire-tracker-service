package app.scheduled

import app.useCase.UpdatePredictionDataUseCase
import app.useCase.UpdateRealTimeDataUseCase
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledActions(
    private val updateRealTimeDataUseCase: UpdateRealTimeDataUseCase,
    private val updatePredictionDataUseCase: UpdatePredictionDataUseCase
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(cron = "0 */5 * * * ?", zone = "Asia/Almaty")
    fun runScheduledRealTimeFireMonitoring() {
        logger.info("Scheduled real-time fire monitoring updates started")
        updateRealTimeDataUseCase.handle()
    }

    @Scheduled(cron = "0 */5 * * * ?", zone = "Asia/Almaty")
    fun runScheduledPredictionFireMonitoring() {
        logger.info("Scheduled prediction fire monitoring updates started")
        updatePredictionDataUseCase.handle()
    }
}
