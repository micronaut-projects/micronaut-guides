package example.micronaut

import com.nimbusds.jose.JWSAlgorithm
import groovy.transform.CompileStatic
import io.micronaut.security.token.jwt.signature.rsa.RSASignatureGeneratorConfiguration

import java.security.interfaces.RSAPrivateKey

@CompileStatic
abstract class AbstractRSAGeneratorSignatureConfiguration extends AbstractRSASignatureConfiguration
        implements RSASignatureGeneratorConfiguration { // <1>

    protected AbstractRSAGeneratorSignatureConfiguration(String jsonJwk) {
        super(jsonJwk)
    }

    @Override
    RSAPrivateKey getPrivateKey() {
        super.privateKey
    }

    @Override
    JWSAlgorithm getJwsAlgorithm() {
        super.jwsAlgorithm
    }
}
