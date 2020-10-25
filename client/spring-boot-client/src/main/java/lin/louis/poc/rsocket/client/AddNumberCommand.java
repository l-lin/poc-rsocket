package lin.louis.poc.rsocket.client;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import reactor.core.publisher.Mono;

@ShellComponent
@Slf4j
@AllArgsConstructor
public class AddNumberCommand {

    private final Mono<RSocketRequester> requesterMono;

    @ShellMethod("Add number")
    public void addNumber(int nb) {
        requesterMono.block()
                .route("add-number")
                .data(nb)
                .send()
                .subscribe();
    }

    @ShellMethod("Add number in stream")
    public void addNumberStream(int step, int nbTimesToEmit) {
        requesterMono.block()
                .route("add-number-infinitely")
                .data(step)
                .retrieveFlux(Integer.class)
                .doOnNext(i -> LOGGER.info("Received number {}", i))
                .doOnError(t -> LOGGER.error(t.getMessage(), t))
                .take(nbTimesToEmit)
                .blockLast();
    }
}
