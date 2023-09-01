package app.core.prometheus

import app.useCase.AnalyzeResponseElement
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.Counter
import org.springframework.stereotype.Service

@Service
class PrometheusService(registry: CollectorRegistry) {

	private val fireDangerMetrics: Counter = Counter.Builder()
		.name("fire_tracker_service_fire_danger_counter")
		.help("Counter for containing fire danger data by location")
		.labelNames("latitude", "longitude", "fireDanger", "fireRadius")
		.register(registry)

	private val externalServiceCallCounter: Counter = Counter.Builder()
		.name("fire_tracker_service_external_service_call_counter")
		.help("Simple counter for external services calls")
		.labelNames("url")
		.register(registry)

	private val externalServiceErrorCounter: Counter = Counter.Builder()
		.name("fire_tracker_service_external_service_error_counter")
		.help("Simple counter for external services calls")
		.labelNames("url")
		.register(registry)

	fun putFireDangerData(data: AnalyzeResponseElement) {
		fireDangerMetrics.labels(
			data.latitude.toString(),
			data.longitude.toString(),
			data.fireDangerRate.toString(),
			data.fireDangerRadius.toString()
		).inc()
	}

	fun incrementExternalServiceCall(url: String) {
		externalServiceCallCounter.labels(url).inc()
	}

	fun incrementExternalServiceError(url: String) {
		externalServiceErrorCounter.labels(url).inc()
	}
}
