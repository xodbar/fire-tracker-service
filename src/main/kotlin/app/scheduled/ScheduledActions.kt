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

    @Scheduled(cron = "*/10 * * * * ?", zone = "Asia/Almaty")
    fun runScheduledRealTimeFireMonitoring() {
        logger.info("Scheduled real-time fire monitoring updates started")
        updateRealTimeDataUseCase.handle()
        logger.info("Scheduled real-time fire monitoring updates finished")
    }

    @Scheduled(cron = "*/35 * * * * ?", zone = "Asia/Almaty")
    fun runScheduledPredictionFireMonitoring() {
        logger.info("Scheduled prediction fire monitoring updates started")
        updatePredictionDataUseCase.handle()
        logger.info("Scheduled prediction fire monitoring updates finished")
    }
}
