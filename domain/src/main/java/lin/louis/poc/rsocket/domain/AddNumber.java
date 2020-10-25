package lin.louis.poc.rsocket.domain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Usecase to showcase fire & forget and request/stream models.
 */
@Slf4j
public class AddNumber {

    private final AtomicInteger nb = new AtomicInteger();

    public Mono<Integer> add(int nbToAdd) {
        return Mono.fromSupplier(() -> {
            int result = nb.accumulateAndGet(nbToAdd, Integer::sum);
            LOGGER.info("Incrementing to {}", result);
            return result;
        });
    }
}
