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
public class SayHelloCommand {

    private final Mono<RSocketRequester> requesterMono;

    @ShellMethod("say hello")
    public String sayHello(String name) {
        return requesterMono.block()
                .route("say-hello")
                .data(name)
                .retrieveMono(String.class)
                .doOnSuccess(LOGGER::info)
                .doOnError(t -> LOGGER.error(t.getMessage(), t))
                .block();
    }
}
