package com.arth.kafkademo.customer.service

import com.arth.kafkademo.customer.network.model.Customer

interface CustomerService {

    fun save(customer: Customer)

}
