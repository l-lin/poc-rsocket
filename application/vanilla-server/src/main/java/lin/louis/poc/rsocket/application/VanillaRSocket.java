package lin.louis.poc.rsocket.application;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.util.DefaultPayload;
import lin.louis.poc.rsocket.adapter.CountController;
import lin.louis.poc.rsocket.adapter.HelloController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@AllArgsConstructor
public class VanillaRSocket implements RSocket {

    private final HelloController helloController;

    private final CountController countController;

    @Override
    public Mono<Payload> requestResponse(final Payload payload) {
        return helloController.greet(payload.getDataUtf8())
                .map(DefaultPayload::create);
    }

    @Override
    public Mono<Void> fireAndForget(final Payload payload) {
        return countController.addNumber(Integer.parseInt(payload.getDataUtf8()));
    }

    @Override
    public Flux<Payload> requestStream(final Payload payload) {
        return countController.addNumberInfinitely(Integer.parseInt(payload.getDataUtf8()))
                .doOnRequest(limitRate -> LOGGER.info("Received request with limit rate set to {}", limitRate))
                .map(integer -> DefaultPayload.create(Integer.toString(integer)));
    }

    @Override
    public Flux<Payload> requestChannel(final Publisher<Payload> payloads) {
        return countController.countEvenNumbers(Flux.from(payloads)
                .map(Payload::getDataUtf8)
                .map(Integer::parseInt))
                .map(integer -> {
                    if (integer % 3 == 0) {
                        // Simulating a request to the client by having a different metadata
                        return DefaultPayload.create("", "say hello for me");
                    }
                    return DefaultPayload.create(Integer.toString(integer));
                });
    }
}
