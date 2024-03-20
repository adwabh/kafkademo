package com.arth.kafkademo.customer.kafka

import com.arth.kafkademo.customer.network.model.Customer
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.function.Function

@Configuration
class Config {

    @Bean
    fun provideMapper(): ObjectMapper = ObjectMapper()

    @Bean
    fun customerKafkaProducer(objectMapper: ObjectMapper): Function<Customer, String> = Function {
        customer -> objectMapper.writeValueAsString(customer)
    }
}