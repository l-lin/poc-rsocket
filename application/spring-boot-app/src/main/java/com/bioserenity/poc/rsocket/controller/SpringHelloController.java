package com.bioserenity.poc.rsocket.controller;

import lin.louis.poc.rsocket.adapter.HelloController;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class SpringHelloController {

    private final HelloController helloController;

    @MessageMapping(value = "say-hello")
    public Mono<String> sayHello(String name) {
        return helloController.greet(name);
    }
}
