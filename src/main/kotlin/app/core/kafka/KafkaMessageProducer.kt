package app.core.kafka

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaMessageProducer(
    @Qualifier("defaultKafkaTemplate")
    private val kafkaTemplate: KafkaTemplate<String, String>
) {

    fun sendMessage(topic: String, message: String) {
        kafkaTemplate.send(topic, message)
    }
}
