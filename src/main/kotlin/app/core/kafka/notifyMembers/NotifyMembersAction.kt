package app.core.kafka.notifyMembers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.io.Serializable

@Component
class NotifyMembersAction {

    @KafkaListener(id = "telegram_consumer_group", topics = ["\${app.topics.notify-consumers}"])
    fun notifyConsumersInTelegram(value: String) {
        val input = jacksonObjectMapper().readValue(value, NotifyConsumersInput::class.java)
        println("mocked telegram: ${input.latitude}, ${input.longitude}, ${input.riskRate}")
    }

    @KafkaListener(id = "email_consumer_group", topics = ["\${app.topics.notify-consumers}"])
    fun notifyConsumersInEmail(value: String) {
        val input = jacksonObjectMapper().readValue(value, NotifyConsumersInput::class.java)
        println("mocked email: ${input.latitude}, ${input.longitude}, ${input.riskRate}")
    }
}

data class NotifyConsumersInput(
    val latitude: Double,
    val longitude: Double,
    val riskRate: Int
) : Serializable
