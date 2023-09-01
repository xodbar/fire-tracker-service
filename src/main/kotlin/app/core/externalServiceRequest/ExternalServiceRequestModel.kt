package app.core.externalServiceRequest

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "external_service_request")
class ExternalServiceRequestModel(

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Long,

	@Column(name = "url", nullable = false)
	val url: String,

	@Column(name = "method", nullable = false)
	val method: String,

	@Column(name = "request", nullable = false)
	val request: String,

	@Column(name = "response")
	var response: String? = null,

	@Column(name = "timestamp", nullable = false)
	val timestamp: LocalDateTime
) {

	fun toDto() = ExternalServiceRequest(
		id = id,
		url = url,
		method = method,
		request = request,
		response = response,
		timestamp = timestamp
	)
}
