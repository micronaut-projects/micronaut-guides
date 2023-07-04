package example.micronaut

import io.micronaut.runtime.context.scope.Refreshable
import jakarta.inject.Named
@Refreshable // <1>
@Named("secondary")
class SecondarySignatureConfiguration(jwkConfiguration: JwkConfiguration) :
        AbstractRSASignatureConfiguration(jwkConfiguration.secondary)
