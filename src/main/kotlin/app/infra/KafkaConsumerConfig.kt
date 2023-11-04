package app.infra

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@EnableKafka
@Configuration
class KafkaConsumerConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapAddress: String,
    @Value("\${spring.kafka.consumer.group-id}")
    private val consumerGroupId: String
) {

    @Bean
    @Qualifier("kafkaConsumerFactory")
    fun getConsumerFactory(): ConsumerFactory<String, String> {
        val properties = hashMapOf<String, Any>()

        properties[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        properties[ConsumerConfig.GROUP_ID_CONFIG] = consumerGroupId
        properties[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        properties[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java

        return DefaultKafkaConsumerFactory(properties)
    }

    @Bean
    @Qualifier("kafkaListenerContainerFactory")
    fun getListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = getConsumerFactory()

        return factory
    }
}
