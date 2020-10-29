package lin.louis.poc.rsocket.application;

import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.core.Resume;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.transport.netty.server.WebsocketServerTransport;
import lin.louis.poc.rsocket.adapter.CountController;
import lin.louis.poc.rsocket.adapter.HelloController;
import lin.louis.poc.rsocket.domain.AddNumber;
import lin.louis.poc.rsocket.domain.CountEvenNumbers;
import lin.louis.poc.rsocket.domain.SayHello;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.util.retry.Retry;

import java.time.Duration;

@Slf4j
@AllArgsConstructor
public class VanillaApp {

    private static final int TCP_PORT = 7000;
    private static final int WS_PORT = 7001;

    public static void main(String[] args) {
        SocketAcceptor socketAcceptor = SocketAcceptor.with(new VanillaRSocket(
                new HelloController(new SayHello()),
                new CountController(new AddNumber(), new CountEvenNumbers())
        ));
        Resume resume = new Resume()
                .retry(Retry.fixedDelay(10, Duration.ofSeconds(1))
                        .doBeforeRetry(retrySignal -> LOGGER.info("Disconnected, trying to reconnect..."))
                );

        CloseableChannel server = RSocketServer.create()
                .acceptor(socketAcceptor)
                .resume(resume)
                .bindNow(TcpServerTransport.create(TCP_PORT));

        RSocketServer.create()
                .acceptor(socketAcceptor)
                .resume(resume)
                .bindNow(WebsocketServerTransport.create(WS_PORT));

        LOGGER.info("Running vanilla app on TCP port {} and WS port {}", TCP_PORT, WS_PORT);
        server.onClose().block();
    }
}
