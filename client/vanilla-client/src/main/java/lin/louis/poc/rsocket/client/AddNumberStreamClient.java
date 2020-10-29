package lin.louis.poc.rsocket.client;

import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lin.louis.poc.rsocket.domain.mapper.PayloadMapper;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class AddNumberStreamClient {

    public static void main(String[] args) throws InterruptedException {
        RSocket rsocket = RSocketConnector.create()
                .connect(TcpClientTransport.create(7000))
                .block();

        int step = new SecureRandom().nextInt(10);
        CountDownLatch latch = new CountDownLatch(1);

        int max = 20;
        int limitRate = 10;
        LOGGER.info("Taking {} items with limit rate set to {}", max, limitRate);
        rsocket.requestStream(DefaultPayload.create(BigInteger.valueOf(step).toByteArray()))
                // backpressure by limiting to 8 requests (= 75% of 10)
                .limitRate(limitRate)
                .doOnRequest(ignored -> LOGGER.info("Sent number {}", step))
                .map(PayloadMapper::toInt)
                .doOnNext(i -> LOGGER.info("Received number {}", i))
                .doOnError(t -> LOGGER.error(t.getMessage(), t))
                .take(max)
                .doFinally(signalType -> {
                    rsocket.dispose();
                    latch.countDown();
                })
                .subscribe();

        latch.await();
    }
}
