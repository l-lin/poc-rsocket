package lin.louis.poc.rsocket.domain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Usecase to showcase request/response model.
 */
@Slf4j
public class SayHello {

    public Mono<String> to(String name) {
        return Mono.fromRunnable(() -> LOGGER.info("Saying hello to {}", name))
                .then(Mono.just("Hello, " + name));
    }
}
