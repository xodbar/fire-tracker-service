package app.infra

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaProducerConfig(
    @Value("\${spring.kafka.bootstrap-servers}")
    private val bootstrapAddress: String
) {

    @Bean
    @Qualifier("defaultKafkaProducer")
    fun getKafkaProducerFactory(): ProducerFactory<String, String> {
        val configurationProperties: HashMap<String, Any> = hashMapOf()

        configurationProperties[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        configurationProperties[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configurationProperties[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java

        return DefaultKafkaProducerFactory(configurationProperties)
    }

    @Bean
    @Qualifier("defaultKafkaTemplate")
    fun getKafkaTemplate() = KafkaTemplate(getKafkaProducerFactory())
}
