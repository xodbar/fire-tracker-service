package app.core.location

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SensitiveLocationRepo : JpaRepository<SensitiveLocationModel, Long> {

    @Query(
        value = "SELECT * FROM sensitive_location " +
                "WHERE latitude BETWEEN :minLatitude AND :maxLatitude " +
                "AND longitude BETWEEN :minLongitude AND :maxLongitude",
        nativeQuery = true
    )
    fun findLocationsInRange(
        @Param("minLatitude") minLatitude: Double,
        @Param("maxLatitude") maxLatitude: Double,
        @Param("minLongitude") minLongitude: Double,
        @Param("maxLongitude") maxLongitude: Double
    ): List<SensitiveLocationModel>
}
