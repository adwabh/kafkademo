package com.arth.kafkademo.customer

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@EnableWebFlux
@Configuration
class WebFluxConfig: WebFluxConfigurer {

}