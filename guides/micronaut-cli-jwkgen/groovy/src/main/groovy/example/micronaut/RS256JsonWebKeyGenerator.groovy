package example.micronaut

import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import jakarta.inject.Singleton

import static com.nimbusds.jose.JWSAlgorithm.RS256
import static com.nimbusds.jose.jwk.KeyUse.SIGNATURE

@Slf4j
@CompileStatic
@Singleton // <1>
class RS256JsonWebKeyGenerator implements JsonWebKeyGenerator {

    @Override
    @NonNull
    Optional<String> generateJsonWebKey(@Nullable String kid) {
        try {
            Optional.of(new RSAKeyGenerator(2048)
                    .algorithm(RS256)
                    .keyUse(SIGNATURE) // indicate the intended use of the key
                    .keyID(kid != null ? kid : generateKid()) // give the key a unique ID
                    .generate()
                    .toJSONString())
        } catch (JOSEException e) {
            log.warn('unable to generate RS256 key', e)
            Optional.empty()
        }
    }

    private String generateKid() {
        UUID.randomUUID().toString().replaceAll('-', '')
    }
}
