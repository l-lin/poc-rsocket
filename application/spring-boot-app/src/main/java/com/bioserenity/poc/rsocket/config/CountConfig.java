package com.bioserenity.poc.rsocket.config;

import lin.louis.poc.rsocket.adapter.CountController;
import lin.louis.poc.rsocket.domain.AddNumber;
import lin.louis.poc.rsocket.domain.CountEvenNumbers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountConfig {

    @Bean
    AddNumber addNumber() {
        return new AddNumber();
    }

    @Bean
    CountEvenNumbers countEvenNumbers() {
        return new CountEvenNumbers();
    }

    @Bean
    CountController countController(AddNumber addNumber, CountEvenNumbers countEvenNumbers) {
        return new CountController(addNumber, countEvenNumbers);
    }
}
