package app.core.weather

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import kotlin.random.Random

interface OpenWeatherService {
    fun getFireRiskRate(latitude: Double, longitude: Double): WeatherData
}

@Service
@ConditionalOnProperty(name = ["\${app.services.open-weather.mocked}"], havingValue = "false")
class OpenWeatherServiceImpl(
    @Qualifier("defaultRestTemplate") private val restTemplate: RestTemplate,
    @Value("\${app.services.open-weather.url}") private val openWeatherUrl: String,
    @Value("\${app.services.open-weather.token}") private val openWeatherToken: String
) : OpenWeatherService {

    override fun getFireRiskRate(latitude: Double, longitude: Double): WeatherData {
        return restTemplate.getForEntity(
            getUrl(latitude, longitude),
            WeatherData::class.java
        ).body ?: throw RuntimeException("Failed to get fire risk rate")
    }

    private fun getUrl(latitude: Double, longitude: Double) =
        "$openWeatherUrl/fwi?lat=$latitude&lon=$longitude&appid=$openWeatherToken"
}

@Service
@ConditionalOnProperty(name = ["\${app.services.open-weather.mocked}"], havingValue = "true", matchIfMissing = true)
class OpenWeatherServiceMocked : OpenWeatherService {
    override fun getFireRiskRate(latitude: Double, longitude: Double) = WeatherData(
        coord = Coord(
            lon = longitude,
            lat = latitude
        ),
        list = listOf(
            WeatherItem(
                main = Main(
                    fwi = 1
                ),
                danger_rating = DangerRating(
                    value = Random.nextInt(1, 3 + 1),
                    description = "mocked"
                ),
                dt = 1L
            )
        )
    )
}

data class Main(
    val fwi: Int
)

data class Coord(
    val lon: Double,
    val lat: Double
)

data class DangerRating(
    val description: String,
    val value: Int
)

data class WeatherData(
    val coord: Coord,
    val list: List<WeatherItem>
) {
    fun getAverageRiskRate(): Int {
        val rates = list.sumOf { it.danger_rating.value }
        return (rates / list.size)
    }
}

data class WeatherItem(
    val main: Main,
    val danger_rating: DangerRating,
    val dt: Long
)
