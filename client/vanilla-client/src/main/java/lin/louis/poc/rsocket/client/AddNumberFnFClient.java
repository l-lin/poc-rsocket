package lin.louis.poc.rsocket.client;

import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.security.SecureRandom;

@Slf4j
public class AddNumberFnFClient {

    public static void main(String[] args) {
        RSocket rsocket = RSocketConnector.create()
                .connect(TcpClientTransport.create(7000))
                .block();

        int nb = new SecureRandom().nextInt(100);

        rsocket.fireAndForget(DefaultPayload.create(BigInteger.valueOf(nb).toByteArray()))
                .doOnSuccess(ignored -> LOGGER.info("Sent number {}", nb))
                .doOnError(t -> LOGGER.error(t.getMessage(), t))
                .block();
    }
}
