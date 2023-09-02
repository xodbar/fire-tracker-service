package app.core.location

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "sensitive_location")
class SensitiveLocationModel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(name = "latitude")
    val latitude: Double,

    @Column(name = "longitude")
    val longitude: Double,

    @Column(name = "last_updated_at")
    var lastUpdatedAt: LocalDateTime?,

    @Column(name = "last_risk_rate")
    var lastRiskRate: Int?
) {
    fun toDto() = SensitiveLocation(id, latitude, longitude, lastUpdatedAt, lastRiskRate)
}
