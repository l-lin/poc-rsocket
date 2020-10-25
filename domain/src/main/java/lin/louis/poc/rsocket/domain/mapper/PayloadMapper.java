package lin.louis.poc.rsocket.domain.mapper;

import io.rsocket.Payload;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * Simple mapper for better readability when looking at the code
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PayloadMapper {

    public static int toInt(Payload payload) {
        byte[] b = new byte[payload.data().readableBytes()];
        payload.data().readBytes(b);
        return new BigInteger(b).intValue();
    }
}
