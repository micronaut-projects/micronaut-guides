package example.micronaut

import com.nimbusds.jose.Algorithm
import com.nimbusds.jose.JOSEException
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWK
import com.nimbusds.jose.jwk.RSAKey
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import io.micronaut.context.exceptions.ConfigurationException
import io.micronaut.core.annotation.NonNull
import io.micronaut.security.token.jwt.signature.rsa.RSASignatureConfiguration

import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.text.ParseException

@Slf4j
@CompileStatic
abstract class AbstractRSASignatureConfiguration
        implements RSASignatureConfiguration { // <1>

    protected final JWK publicJWK
    protected final RSAPublicKey publicKey
    protected final RSAPrivateKey privateKey
    protected final JWSAlgorithm jwsAlgorithm

    AbstractRSASignatureConfiguration(String jsonJwk) {
        RSAKey primaryRSAKey = parseRSAKey(jsonJwk)
                .orElseThrow(() -> new ConfigurationException('could not parse primary JWK to RSA Key'))

        publicJWK = primaryRSAKey.toPublicJWK()

        try {
            privateKey = primaryRSAKey.toRSAPrivateKey()
        } catch (JOSEException ignored) {
            throw new ConfigurationException('could not primary RSA private key')
        }

        try {
            publicKey = primaryRSAKey.toRSAPublicKey()
        } catch (JOSEException ignored) {
            throw new ConfigurationException('could not primary RSA public key')
        }

        jwsAlgorithm = parseJWSAlgorithm(primaryRSAKey)
                .orElseThrow(() -> new ConfigurationException('could not parse JWS Algorithm from RSA Key'))
    }

    @NonNull
    JWK getPublicJWK() {
        publicJWK
    }

    @Override
    RSAPublicKey getPublicKey() {
        publicKey
    }

    @NonNull
    private Optional<JWSAlgorithm> parseJWSAlgorithm(@NonNull RSAKey rsaKey) {
        Algorithm algorithm = rsaKey.algorithm
        if (!algorithm) {
            return Optional.empty()
        }

        if (algorithm instanceof JWSAlgorithm) {
            return Optional.of((JWSAlgorithm) algorithm)
        }

        Optional.of(JWSAlgorithm.parse(algorithm.name))
    }

    @NonNull
    private Optional<RSAKey> parseRSAKey(@NonNull String jsonJwk) {
        try {
            JWK jwk = JWK.parse(jsonJwk)
            if (!(jwk instanceof RSAKey)) {
                log.warn('JWK is not an RSAKey')
                return Optional.empty()
            }
            Optional.of((RSAKey) jwk)
        } catch (ParseException ignored) {
            log.warn('Could not parse JWK JSON string {}', jsonJwk)
            Optional.empty()
        }
    }
}
