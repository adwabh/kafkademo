package kafka.e2e.customer.service

import com.arth.kafkademo.customer.network.model.Customer
import com.arth.kafkademo.customer.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.function.Function

@Service
class CustomerServiceImpl(@Autowired private val customerKafkaProducer: Function<Customer, Unit>) :
    CustomerService {

    override fun save(customer: Customer) {
        customerKafkaProducer.apply(customer)
    }
}