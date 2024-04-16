package com.arth.kafkademo.customer.kafka

import com.arth.kafkademo.customer.network.model.Customer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import reactor.core.publisher.Flux
import reactor.kafka.receiver.ReceiverOffset
import reactor.kafka.sender.SenderOptions
import java.util.function.Consumer
import java.util.function.Function


@Configuration
class Config {

    @Bean
    fun reactiveKafkaProducerTemplate(): ReactiveKafkaProducerTemplate<String, String> {
        val props: MutableMap<String, Any> = HashMap()
        val bootstrapServers = "localhost:9092"
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ProducerConfig.CLIENT_ID_CONFIG] = "sample-producer"
        props[ProducerConfig.ACKS_CONFIG] = "all"
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        val senderOptions: SenderOptions<String, String> = SenderOptions.create(props)
        return ReactiveKafkaProducerTemplate(senderOptions)
    }

    @Bean
    fun provideMapper(): ObjectMapper = jacksonObjectMapper()

    @Bean
    fun provideSerializer(objectMapper: ObjectMapper): Serializer<Customer> =
        Serializer<Customer> { _, data -> objectMapper.writeValueAsString(data).toByteArray() }

    @Bean
    fun customerKafkaProducer(
        objectMapper: ObjectMapper,
        template: ReactiveKafkaProducerTemplate<String, String>
    ): Function<Customer, Unit> = Function { customer ->
        val custJson = objectMapper.writeValueAsString(customer)
        template.send("customer_out_topic", custJson).subscribe()
    }

    @Bean
    fun incomingCustomer(objectMapper: ObjectMapper): Consumer<Flux<Message<String>>> =
        Consumer { input ->
            input.doOnNext {
                println("Received customer: ${it.payload}")
                val customer: Customer = objectMapper.readValue(it.payload, Customer::class.java)
                println("parsed value: $customer")
                it.acknowledgeKafkaMessage()
            }.subscribe()
        }
}

private fun <T> Message<T>.acknowledgeKafkaMessage() =
    headers.get(KafkaHeaders.ACKNOWLEDGMENT, ReceiverOffset::class.java)?.acknowledge()
