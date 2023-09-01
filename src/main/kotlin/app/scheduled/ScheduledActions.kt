package app.scheduled

import app.useCase.UpdateFirmsDataUseCase
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledActions(
	private val updateFirmsDataUseCase: UpdateFirmsDataUseCase
) {

	private val logger = LoggerFactory.getLogger(this::class.java)

	@Scheduled(cron = "0 */5 * * * ?", zone = "Asia/Almaty")
	fun runScheduledFireMonitoring() {
		logger.info("Scheduled fire monitoring updates started")
		updateFirmsDataUseCase.handle()
	}
}
