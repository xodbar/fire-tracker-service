package app.core.externalServiceRequest

import app.core.utils.getCurrentAlmatyLocalDateTime
import org.springframework.stereotype.Service

@Service
class ExternalServiceRequestManager(
	private val externalServiceRequestRepo: ExternalServiceRequestRepo
) {

	fun saveRequest(url: String, method: String, request: String, response: String) = externalServiceRequestRepo.save(
		ExternalServiceRequestModel(
			id = -1L,
			url = url,
			method = method,
			request = request,
			response = response,
			timestamp = getCurrentAlmatyLocalDateTime()
		)
	).toDto()
}
