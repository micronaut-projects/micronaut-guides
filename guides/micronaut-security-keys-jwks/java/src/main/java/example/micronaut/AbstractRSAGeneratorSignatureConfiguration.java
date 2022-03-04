package example.micronaut;

import com.nimbusds.jose.JWSAlgorithm;
import io.micronaut.security.token.jwt.signature.rsa.RSASignatureGeneratorConfiguration;

import java.security.interfaces.RSAPrivateKey;

public abstract class AbstractRSAGeneratorSignatureConfiguration extends AbstractRSASignatureConfiguration
        implements RSASignatureGeneratorConfiguration { // <1>

    protected AbstractRSAGeneratorSignatureConfiguration(String jsonJwk) {
        super(jsonJwk);
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return privateKey;
    }

    @Override
    public JWSAlgorithm getJwsAlgorithm() {
        return jwsAlgorithm;
    }
}
