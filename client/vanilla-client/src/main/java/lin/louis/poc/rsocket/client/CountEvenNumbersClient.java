package lin.louis.poc.rsocket.client;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class CountEvenNumbersClient {

    public static void main(String[] args) throws InterruptedException {
        RSocket rsocket = RSocketConnector.create()
                .connect(TcpClientTransport.create(7000))
                .block();

        CountDownLatch latch = new CountDownLatch(1);
        Random random = new SecureRandom();

        // publish random numbers every 0.5s
        Flux<Payload> publisher = Flux.interval(Duration.ofMillis(500))
                .map(ignored -> random.nextInt(100))
                .map(nb -> {
                    LOGGER.info("Sent number {}", nb);
                    return DefaultPayload.create(BigInteger.valueOf(nb).toByteArray());
                });

        rsocket.requestChannel(publisher)
                .doOnError(t -> LOGGER.error(t.getMessage(), t))
                .take(10)
                .doFinally(signalType -> {
                    rsocket.dispose();
                    latch.countDown();
                })
                .subscribe(payload -> {
                    if (payload.hasMetadata()) {
                        rsocket.requestResponse(DefaultPayload.create("everyone"))
                                .map(Payload::getDataUtf8)
                                .doOnNext(LOGGER::info)
                                .doOnError(t -> LOGGER.error(t.getMessage(), t))
                                .subscribe();
                    } else {
                        int i = Integer.parseInt(payload.getDataUtf8());
                        LOGGER.info("Received nb even numbers: {}", i);
                    }
                });

        latch.await();
    }
}
