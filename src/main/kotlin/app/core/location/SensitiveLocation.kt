package app.core.location

import java.io.Serializable
import java.time.LocalDateTime

data class SensitiveLocation(
    val id: Long,
    val latitude: Double,
    val longitude: Double,
    val lastUpdatedAt: LocalDateTime?,
    val lastRiskRate: Int?
) : Serializable
