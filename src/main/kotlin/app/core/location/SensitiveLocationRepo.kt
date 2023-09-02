package app.core.location

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SensitiveLocationRepo : JpaRepository<SensitiveLocationModel, Long> {
    fun findFirstByLatitudeAndLongitude(latitude: Double, longitude: Double): SensitiveLocationModel?
}
