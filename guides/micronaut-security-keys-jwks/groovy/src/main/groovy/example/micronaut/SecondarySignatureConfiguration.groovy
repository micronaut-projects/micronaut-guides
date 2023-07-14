package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.runtime.context.scope.Refreshable
import jakarta.inject.Named

@CompileStatic
@Refreshable // <1>
@Named("secondary")
class SecondarySignatureConfiguration extends AbstractRSASignatureConfiguration {

    SecondarySignatureConfiguration(JwkConfiguration jwkConfiguration) {
        super(jwkConfiguration.secondary)
    }
}
