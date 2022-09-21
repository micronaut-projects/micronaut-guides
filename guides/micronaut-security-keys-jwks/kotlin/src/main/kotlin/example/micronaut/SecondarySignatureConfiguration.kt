package example.micronaut

import io.micronaut.runtime.context.scope.Refreshable

@Refreshable // <1>
class SecondarySignatureConfiguration(jwkConfiguration: JwkConfiguration) :
        AbstractRSASignatureConfiguration(jwkConfiguration.secondary)
