package app.web.controller

import app.core.utils.getCurrentAlmatyLocalDateTime
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import java.io.Serializable
import java.time.LocalDateTime

@RestController
@RequestMapping("/health")
class HealthController(
    @Value("\${app.version}") private val apiVersion: String
) {

    @GetMapping
    @ResponseBody
    fun health() = HealthResponse(
        apiVersion = apiVersion,
        timestamp = getCurrentAlmatyLocalDateTime()
    )
}

data class HealthResponse(
    val apiVersion: String,
    val timestamp: LocalDateTime
) : Serializable
