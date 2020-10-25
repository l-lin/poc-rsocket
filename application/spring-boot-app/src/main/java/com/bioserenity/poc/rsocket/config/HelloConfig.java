package com.bioserenity.poc.rsocket.config;

import lin.louis.poc.rsocket.adapter.HelloController;
import lin.louis.poc.rsocket.domain.SayHello;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HelloConfig {

    @Bean
    SayHello sayHello() {
        return new SayHello();
    }

    @Bean
    HelloController helloController(SayHello sayHello) {
        return new HelloController(sayHello);
    }
}
