package example.micronaut;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.UUID;

@Singleton // <1>
public class RS256JsonWebKeyGenerator implements JsonWebKeyGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(RS256JsonWebKeyGenerator.class);

    @Override
    @NonNull
    public Optional<String> generateJsonWebKey(@Nullable String kid) {
        try {
            return Optional.of(new RSAKeyGenerator(2048)
                    .algorithm(JWSAlgorithm.RS256)
                    .keyUse(KeyUse.SIGNATURE) // indicate the intended use of the key
                    .keyID(kid != null ? kid : generateKid()) // give the key a unique ID
                    .generate()
                    .toJSONString());

        } catch (JOSEException e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("unable to generate RS256 key",  e);
            }
        }
        return Optional.empty();
    }

    private String generateKid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
