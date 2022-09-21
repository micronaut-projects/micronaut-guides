package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.runtime.context.scope.Refreshable
import jakarta.inject.Named

@CompileStatic
@Refreshable // <1>
@Named('generator') // <2>
class PrimarySignatureConfiguration extends AbstractRSAGeneratorSignatureConfiguration {

    PrimarySignatureConfiguration(JwkConfiguration jwkConfiguration) {
        super(jwkConfiguration.primary)
    }
}
