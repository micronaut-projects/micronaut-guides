package example.micronaut;

import com.nimbusds.jose.jwk.JWK;
import io.micronaut.runtime.context.scope.Refreshable;
import io.micronaut.security.token.jwt.endpoints.JwkProvider;

import java.util.Arrays;
import java.util.List;

@Refreshable // <1>
public class JsonWebKeysProvider implements JwkProvider { // <2>

    private final List<JWK> jwks;

    public JsonWebKeysProvider(PrimarySignatureConfiguration primaryRsaPrivateKey,
                               SecondarySignatureConfiguration secondarySignatureConfiguration) {
        jwks = Arrays.asList(primaryRsaPrivateKey.getPublicJWK(), secondarySignatureConfiguration.getPublicJWK());
    }

    @Override
    public List<JWK> retrieveJsonWebKeys() {
        return jwks;
    }
}
