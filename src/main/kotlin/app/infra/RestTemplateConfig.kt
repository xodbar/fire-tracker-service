package app.infra

import app.core.prometheus.PrometheusService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.util.StopWatch
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig(
	private val prometheusService: PrometheusService
) {

	@Bean
	@Qualifier("defaultRestTemplate")
	fun getDefaultRestTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate = restTemplateBuilder
		.additionalInterceptors(RestTemplateLoggerInterceptor(prometheusService))
		.build()
}

class RestTemplateLoggerInterceptor(
	private val prometheusService: PrometheusService
) : ClientHttpRequestInterceptor {

	override fun intercept(
		request: HttpRequest,
		body: ByteArray,
		execution: ClientHttpRequestExecution
	): ClientHttpResponse {
		val stopWatch = StopWatch()
		stopWatch.start()

		val uri = request.uri.rawPath

		val response = try {
			prometheusService.incrementExternalServiceCall(uri)
			execution.execute(request, body)
		} catch (e: Exception) {
			stopWatch.stop()
			prometheusService.incrementExternalServiceError(uri)
			throw e
		}

		return response
	}
}
