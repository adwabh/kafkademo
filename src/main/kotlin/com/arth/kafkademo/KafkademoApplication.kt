package com.arth.kafkademo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KafkademoApplication

fun main(args: Array<String>) {
	runApplication<KafkademoApplication>(*args)
}
