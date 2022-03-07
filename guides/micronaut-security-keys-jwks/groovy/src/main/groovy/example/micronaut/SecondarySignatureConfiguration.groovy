package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.runtime.context.scope.Refreshable

@CompileStatic
@Refreshable // <1>
class SecondarySignatureConfiguration extends AbstractRSASignatureConfiguration {

    SecondarySignatureConfiguration(JwkConfiguration jwkConfiguration) {
        super(jwkConfiguration.secondary)
    }
}
