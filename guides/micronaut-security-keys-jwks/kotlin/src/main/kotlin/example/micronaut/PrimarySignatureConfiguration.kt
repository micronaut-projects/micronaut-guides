package example.micronaut

import io.micronaut.runtime.context.scope.Refreshable
import jakarta.inject.Named

@Refreshable // <1>
@Named("generator") // <2>
class PrimarySignatureConfiguration(jwkConfiguration: JwkConfiguration) :
        AbstractRSAGeneratorSignatureConfiguration(jwkConfiguration.primary)
