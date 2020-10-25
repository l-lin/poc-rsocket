package lin.louis.poc.rsocket.application;

import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.CloseableChannel;
import io.rsocket.transport.netty.server.TcpServerTransport;
import lin.louis.poc.rsocket.adapter.CountController;
import lin.louis.poc.rsocket.adapter.HelloController;
import lin.louis.poc.rsocket.domain.AddNumber;
import lin.louis.poc.rsocket.domain.CountEvenNumbers;
import lin.louis.poc.rsocket.domain.SayHello;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class VanillaApp {

    private static final int PORT = 7000;

    public static void main(String[] args) {
        CloseableChannel server = RSocketServer.create()
                .acceptor(SocketAcceptor.with(new VanillaRSocket(
                        new HelloController(new SayHello()),
                        new CountController(new AddNumber(), new CountEvenNumbers())
                )))
                .bindNow(TcpServerTransport.create(PORT));

        LOGGER.info("Running vanilla app on port {}", PORT);
        server.onClose().block();
    }
}
