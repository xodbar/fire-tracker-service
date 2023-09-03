package app.core.prometheus

import app.core.utils.getRoundedLatitudeAndLongitude
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.Counter
import io.prometheus.client.Gauge
import org.springframework.stereotype.Service

@Service
class PrometheusService(registry: CollectorRegistry) {

    private val fireDangerMetricsRealTime: Gauge = Gauge.build()
        .name("fire_tracker_service_fire_danger_real_time")
        .help("Real time fire gauge")
        .labelNames("latitude", "longitude")
        .register(registry)

    private val fireDangerMetricsPrediction: Gauge = Gauge.build()
        .name("fire_tracker_service_fire_danger_prediction")
        .help("Prediction fire gauge")
        .labelNames("latitude", "longitude")
        .register(registry)

    private val externalServiceCallCounter: Counter = Counter.Builder()
        .name("fire_tracker_service_external_service_call_counter")
        .help("Simple counter for external services calls")
        .labelNames("url")
        .register(registry)

    private val externalServiceErrorCounter: Counter = Counter.Builder()
        .name("fire_tracker_service_external_service_error_counter")
        .help("Simple counter for external services errors")
        .labelNames("url")
        .register(registry)

    fun putRealTimeData(latitude: Double, longitude: Double, currentRiskRate: Int) {
        val (roundedLatitude, roundedLongitude) = getRoundedLatitudeAndLongitude(latitude, longitude)
        fireDangerMetricsRealTime
            .labels(roundedLatitude.toString(), roundedLongitude.toString()) // Example latitude and longitude
            .set(currentRiskRate.toDouble())
    }

    fun putPredictionData(latitude: Double, longitude: Double, riskRate: Int) {
        val (roundedLatitude, roundedLongitude) = getRoundedLatitudeAndLongitude(latitude, longitude)
        fireDangerMetricsPrediction
            .labels(roundedLatitude.toString(), roundedLongitude.toString()) // Example latitude and longitude
            .set(riskRate.toDouble())
    }

    fun incrementExternalServiceCall(url: String) {
        externalServiceCallCounter.labels(url).inc()
    }

    fun incrementExternalServiceError(url: String) {
        externalServiceErrorCounter.labels(url).inc()
    }
}
