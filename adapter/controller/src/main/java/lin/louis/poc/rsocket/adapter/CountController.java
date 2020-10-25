package lin.louis.poc.rsocket.adapter;

import lin.louis.poc.rsocket.domain.AddNumber;
import lin.louis.poc.rsocket.domain.CountEvenNumbers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@AllArgsConstructor
public class CountController {

    private final AddNumber addNumber;

    private final CountEvenNumbers countEvenNumbers;

    public Mono<Void> addNumber(int nb) {
        return Mono.fromRunnable(() -> LOGGER.info("Adding {}", nb))
                .then(addNumber.add(nb))
                .doOnError(t -> LOGGER.error(t.getMessage(), t))
                .then();
    }

    public Flux<Integer> addNumberInfinitely(int steps) {
        return Mono.fromRunnable(() -> LOGGER.info("Adding number infinitely with step {}", steps))
                .thenMany(Flux.interval(Duration.ofSeconds(1)))
                .flatMap(ignored -> addNumber.add(steps))
                .doOnError(t -> LOGGER.error(t.getMessage(), t));
    }

    public Flux<Integer> countEvenNumbers(Publisher<Integer> publisher) {
        return Mono.fromRunnable(() -> LOGGER.info("Counting even numbers"))
                .thenMany(countEvenNumbers.feed(publisher))
                .doOnError(t -> LOGGER.error(t.getMessage(), t));
    }
}
