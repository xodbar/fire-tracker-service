package app.core.externalServiceRequest

import app.core.utils.getCurrentAlmatyLocalDateTime
import org.springframework.stereotype.Service
import java.util.Base64

@Service
class ExternalServiceRequestManager(
	private val externalServiceRequestRepo: ExternalServiceRequestRepo
) {

	fun saveRequest(url: String, method: String, request: String, response: String) = externalServiceRequestRepo.save(
		ExternalServiceRequestModel(
			id = -1L,
			url = url,
			method = method,
			request = Base64.getEncoder().encode(request.toByteArray()).toString(),
			response = Base64.getEncoder().encode(response.toByteArray()).toString(),
			timestamp = getCurrentAlmatyLocalDateTime()
		)
	).toDto()
}
