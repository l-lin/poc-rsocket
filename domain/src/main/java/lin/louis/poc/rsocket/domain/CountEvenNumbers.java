package lin.louis.poc.rsocket.domain;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Usecase to showcase Channel model.
 */
@Slf4j
@AllArgsConstructor
public class CountEvenNumbers {

    private final AtomicInteger nb = new AtomicInteger();

    public Flux<Integer> feed(Publisher<Integer> publisher) {
        publisher.subscribe(new Subscriber<>() {

            @Override
            public void onSubscribe(final Subscription s) {
                // no backpressure
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(final Integer i) {
                LOGGER.info("Received {}", i);
                if (i % 2 == 0) {
                    nb.incrementAndGet();
                }
            }

            @Override
            public void onError(final Throwable t) {
                LOGGER.error(t.getMessage(), t);
            }

            @Override
            public void onComplete() {
                LOGGER.info("Done");
            }
        });
        // every 2 seconds, send the total number computed to the subscribers
        // ex to simulate a long process in the server
        return Flux.interval(Duration.ofSeconds(2))
                .map(i -> {
                    int total = nb.get();
                    LOGGER.info("Sending total nb: {}", total);
                    return total;
                });
    }
}
