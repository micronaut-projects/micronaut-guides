package example.micronaut

import com.nimbusds.jose.jwk.JWK
import groovy.transform.CompileStatic
import io.micronaut.runtime.context.scope.Refreshable
import io.micronaut.security.token.jwt.endpoints.JwkProvider

@CompileStatic
@Refreshable // <1>
class JsonWebKeysProvider implements  JwkProvider { // <2>

    private final List<JWK> jwks

    JsonWebKeysProvider(PrimarySignatureConfiguration primaryRsaPrivateKey,
                        SecondarySignatureConfiguration secondarySignatureConfiguration) {
        jwks = [primaryRsaPrivateKey.publicJWK, secondarySignatureConfiguration.publicJWK]
    }

    @Override
    List<JWK> retrieveJsonWebKeys() {
        jwks
    }
}
