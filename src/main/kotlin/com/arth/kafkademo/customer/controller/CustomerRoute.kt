package com.arth.kafkademo.customer.controller

import com.arth.kafkademo.customer.network.model.Customer
import com.arth.kafkademo.customer.service.CustomerService
import kafka.e2e.customer.service.CustomerServiceImpl
import kotlinx.coroutines.flow.firstOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*
import java.util.function.Function

@Configuration
class CustomerRoute {

    @Bean
    fun customerService(customerKafkaProducer: Function<Customer, String>): CustomerService {
        return CustomerServiceImpl(customerKafkaProducer)
    }

    @Bean
    fun http(@Autowired customerService: CustomerService): RouterFunction<ServerResponse> {
        return coRouter {
            ("/v1").nest {
                pushCustomerRecord(customerService)
            }
        }
    }

    private fun CoRouterFunctionDsl.pushCustomerRecord(customerService: CustomerService) = POST("/customer") { serverRequest ->
        serverRequest.bodyToFlow<Customer>()
                .firstOrNull()?.let {
                    customerService.save(it)
                    ServerResponse.ok().bodyValueAndAwait("Customer saved")
                } ?: ServerResponse.badRequest().bodyValueAndAwait("Invalid customer")
    }
}