package example.micronaut

import com.nimbusds.jose.jwk.JWK
import io.micronaut.runtime.context.scope.Refreshable
import io.micronaut.security.token.jwt.endpoints.JwkProvider

@Refreshable // <1>
class JsonWebKeysProvider(primaryRsaPrivateKey: PrimarySignatureConfiguration,
                          secondarySignatureConfiguration: SecondarySignatureConfiguration) : JwkProvider { // <2>

    private val jwks: List<JWK>

    init {
        jwks = listOf(primaryRsaPrivateKey.publicJWK, secondarySignatureConfiguration.publicJWK)
    }

    override fun retrieveJsonWebKeys(): List<JWK> = jwks
}
