package app.core.location

import app.core.utils.getCurrentAlmatyLocalDateTime
import app.core.utils.getRoundedLatitudeAndLongitude
import org.springframework.stereotype.Service

@Service
class SensitiveLocationService(
    private val sensitiveLocationRepo: SensitiveLocationRepo
) {

    fun getAll() = sensitiveLocationRepo.findAll().map { it.toDto() }

    fun save(latitude: Double, longitude: Double, riskRate: Int) {
        val model = getByLocation(latitude, longitude) ?: run {
            saveNewSensitiveLocation(latitude, longitude, riskRate)
            return
        }

        model.lastUpdatedAt = getCurrentAlmatyLocalDateTime()
        model.lastRiskRate = riskRate
    }

    private fun saveNewSensitiveLocation(latitude: Double, longitude: Double, riskRate: Int) {
        val (roundedLatitude, roundedLongitude) = getRoundedLatitudeAndLongitude(latitude, longitude)
        sensitiveLocationRepo.save(
            SensitiveLocationModel(
                id = -1L,
                latitude = roundedLatitude,
                longitude = roundedLongitude,
                lastUpdatedAt = getCurrentAlmatyLocalDateTime(),
                lastRiskRate = riskRate
            )
        )
    }

    private fun getByLocation(latitude: Double, longitude: Double): SensitiveLocationModel? {
        val (roundedLatitude, roundedLongitude) = getRoundedLatitudeAndLongitude(latitude, longitude)
        return sensitiveLocationRepo.findFirstByLatitudeAndLongitude(roundedLatitude, roundedLongitude)
    }
}
