package com.bioserenity.poc.rsocket.controller;

import lin.louis.poc.rsocket.adapter.CountController;
import lombok.AllArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
@AllArgsConstructor
public class SpringCountController {

    private final CountController countController;

    @MessageMapping("add-number")
    public Mono<Void> increment(int nb) {
        return countController.addNumber(nb);
    }

    @MessageMapping("add-number-infinitely")
    public Flux<Integer> incrementInfinitely(int steps) {
        return countController.addNumberInfinitely(steps);
    }

    @MessageMapping("count-even-numbers")
    public Flux<Integer> countEvenNumbers(Publisher<Integer> publisher) {
        return countController.countEvenNumbers(publisher);
    }
}
