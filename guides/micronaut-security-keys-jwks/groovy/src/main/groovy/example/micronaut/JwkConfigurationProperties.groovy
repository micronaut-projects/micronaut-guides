package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.annotation.NonNull

import jakarta.validation.constraints.NotBlank

@CompileStatic
@ConfigurationProperties('jwk') // <1>
class JwkConfigurationProperties implements JwkConfiguration {

    @NonNull
    @NotBlank // <2>
    String primary

    @NonNull
    @NotBlank // <2>
    String secondary
}
