package app.core.externalServiceRequest

import java.io.Serializable
import java.time.LocalDateTime

data class ExternalServiceRequest(
	val id: Long,
	val url: String,
	val method: String,
	val request: String,
	val response: String?,
	val timestamp: LocalDateTime
) : Serializable
