package lin.louis.poc.rsocket.client.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@ConfigurationProperties(prefix = "poc")
@Data
@Validated
public class ClientProperties {

    @NotBlank
    private String host;

    @Positive
    private int port;
}
