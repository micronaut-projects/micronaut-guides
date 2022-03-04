package example.micronaut;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import io.micronaut.context.exceptions.ConfigurationException;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.token.jwt.signature.rsa.RSASignatureConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Optional;

public abstract class AbstractRSASignatureConfiguration
        implements RSASignatureConfiguration { // <1>

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRSASignatureConfiguration.class);

    protected final JWK publicJWK;
    protected final RSAPublicKey publicKey;
    protected final RSAPrivateKey privateKey;
    protected final JWSAlgorithm jwsAlgorithm;

    public AbstractRSASignatureConfiguration(String jsonJwk) {
        RSAKey primaryRSAKey = parseRSAKey(jsonJwk)
                .orElseThrow(() -> new ConfigurationException("could not parse primary JWK to RSA Key"));

        publicJWK = primaryRSAKey.toPublicJWK();

        try {
            privateKey = primaryRSAKey.toRSAPrivateKey();
        } catch (JOSEException e) {
            throw new ConfigurationException("could not primary RSA private key");
        }

        try {
            publicKey = primaryRSAKey.toRSAPublicKey();
        } catch (JOSEException e) {
            throw new ConfigurationException("could not primary RSA public key");
        }

        jwsAlgorithm = parseJWSAlgorithm(primaryRSAKey)
                .orElseThrow(() -> new ConfigurationException("could not parse JWS Algorithm from RSA Key"));
    }

    @NonNull
    public JWK getPublicJWK() {
        return publicJWK;
    }

    @Override
    public RSAPublicKey getPublicKey() {
        return publicKey;
    }

    @NonNull
    private Optional<JWSAlgorithm> parseJWSAlgorithm(@NonNull RSAKey rsaKey) {
        Algorithm algorithm = rsaKey.getAlgorithm();
        if (algorithm == null) {
            return Optional.empty();
        }

        if (algorithm instanceof JWSAlgorithm) {
            return Optional.of((JWSAlgorithm) algorithm);
        }

        return Optional.of(JWSAlgorithm.parse(algorithm.getName()));
    }

    @NonNull
    private Optional<RSAKey> parseRSAKey(@NonNull String jsonJwk) {
        try {
            JWK jwk = JWK.parse(jsonJwk);
            if (!(jwk instanceof RSAKey)) {
                LOG.warn("JWK is not an RSAKey");
                return Optional.empty();
            }
            return Optional.of((RSAKey) jwk);
        } catch (ParseException e) {
            LOG.warn("Could not parse JWK JSON string {}", jsonJwk);
            return Optional.empty();
        }
    }
}
