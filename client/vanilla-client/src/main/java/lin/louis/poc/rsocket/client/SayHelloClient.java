package lin.louis.poc.rsocket.client;

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;

@Slf4j
public class SayHelloClient {

    private static final List<String> NAMES = List.of(
            "Bioserenity",
            "Louis",
            "Sylvain",
            "Mohamed",
            "Maxime",
            "Emmanuel",
            "Martin"
    );

    public static void main(String[] args) throws InterruptedException {
        RSocket rsocket = RSocketConnector.create()
                .connect(TcpClientTransport.create(7000))
                .block();

        int nbRequests = 10;
        CountDownLatch latch = new CountDownLatch(nbRequests);

        IntStream.range(0, nbRequests).forEach(ignored -> {
            String name = getRandomName();
            rsocket.requestResponse(DefaultPayload.create(name))
                    .map(Payload::getDataUtf8)
                    .doOnNext(LOGGER::info)
                    .doOnError(t -> LOGGER.error(t.getMessage(), t))
                    .doOnTerminate(latch::countDown)
                    .subscribe();
        });

        latch.await();
    }

    private static String getRandomName() {
        int index = new SecureRandom().nextInt(NAMES.size() - 1) + 1;
        return NAMES.get(index);
    }
}
