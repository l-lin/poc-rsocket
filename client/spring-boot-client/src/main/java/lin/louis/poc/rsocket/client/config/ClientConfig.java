package lin.louis.poc.rsocket.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@EnableConfigurationProperties(ClientProperties.class)
public class ClientConfig {

    @Bean
    Mono<RSocketRequester> requesterMono(ClientProperties clientProperties, RSocketStrategies rSocketStrategies) {
        LOGGER.info("Will connect to RSocket tcp://{}:{}", clientProperties.getHost(), clientProperties.getPort());
        return RSocketRequester.builder()
                .rsocketStrategies(rSocketStrategies)
                .connectTcp(clientProperties.getHost(), clientProperties.getPort());
    }
}
