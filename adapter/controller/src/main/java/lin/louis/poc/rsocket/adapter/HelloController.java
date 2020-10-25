package lin.louis.poc.rsocket.adapter;

import lin.louis.poc.rsocket.domain.SayHello;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class HelloController {

    private final SayHello sayHello;

    public Mono<String> greet(String name) {
        return sayHello.to(name)
                .doOnError(t -> LOGGER.error(t.getMessage(), t));
    }
}
