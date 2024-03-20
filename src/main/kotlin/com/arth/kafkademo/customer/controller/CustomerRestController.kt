package com.arth.kafkademo.customer.controller

import com.arth.kafkademo.customer.network.model.Customer
import com.arth.kafkademo.customer.service.CustomerService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


class CustomerRestController(private val customerService: CustomerService) {

    @PostMapping
    fun save(@RequestBody customer: Customer) {
        customerService.save(customer)
    }

}