package example.micronaut;

import com.nimbusds.jose.JWSAlgorithm;
import io.micronaut.security.token.jwt.signature.rsa.RSASignatureGeneratorConfiguration;

import java.security.interfaces.RSAPrivateKey;

public class AbstractRSAGeneratorSignatureConfiguration extends AbstractRSASignatureConfiguration
        implements RSASignatureGeneratorConfiguration { // <1>

    public AbstractRSAGeneratorSignatureConfiguration(String jsonJwk) {
        super(jsonJwk);
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return this.privateKey;
    }

    @Override
    public JWSAlgorithm getJwsAlgorithm() {
        return this.jwsAlgorithm;
    }
}
