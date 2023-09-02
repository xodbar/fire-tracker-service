package app.core.nasa

import app.core.nasa.token.NasaTokenRepo
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.io.Serializable
import java.time.LocalDate

@Service
class NasaFirmsService(
    private val nasaTokenRepo: NasaTokenRepo,
    @Qualifier("defaultRestTemplate") private val restTemplate: RestTemplate,
    @Value("\${app.services.nasa.url}") private val nasaFirmsUrl: String
) {

    fun getFirmsData(country: SupportedCountry, toDate: LocalDate, range: Int): String {
        return restTemplate.getForEntity(
            getFirmsUrl(country, toDate, range),
            String::class.java
        ).body ?: throw RuntimeException("Failed to get NASA FIRMS data")
    }

    private fun getFirmsUrl(country: SupportedCountry, toDate: LocalDate, range: Int): String {
        val tokenModel = nasaTokenRepo.findFirstByActiveIsTrueOrderByIdDesc()
        return "$nasaFirmsUrl/country/csv/${tokenModel.token}/VIIRS_SNPP_NRT/${country.code}/$range/$toDate"
    }
}

enum class SupportedCountry(val code: String) {
    KAZAKHSTAN("KAZ"),
    UNITED_STATES("USA")
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class NasaFirmsResponse(
    @JsonProperty("list")
    val list: List<NasaFirmsElement>
) : Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class NasaFirmsElement(

    @JsonProperty("country_id")
    val countryId: String?,

    @JsonProperty("latitude")
    val latitude: Double,

    @JsonProperty("longitude")
    val longitude: Double,

    @JsonProperty("bright_ti4")
    val brightTi4: Double?,

    @JsonProperty("scan")
    val scan: Double?,

    @JsonProperty("track")
    val track: Double?,

    @JsonProperty("acq_date")
    val acqDate: String?,

    @JsonProperty("acq_time")
    val acqTime: Int?,

    @JsonProperty("satellite")
    val satellite: String?,

    @JsonProperty("instrument")
    val instrument: String?,

    @JsonProperty("confidence")
    val confidence: String?,

    @JsonProperty("version")
    val version: String?,

    @JsonProperty("bright_ti5")
    val brightTi5: Double,

    @JsonProperty("frp")
    val frp: Double?,

    @JsonProperty("daynight")
    val dayNight: String?
) : Serializable
