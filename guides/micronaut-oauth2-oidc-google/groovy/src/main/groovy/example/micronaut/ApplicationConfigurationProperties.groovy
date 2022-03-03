package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.NonNull

@CompileStatic
@Requires(property = 'app.hosted-domain')
@ConfigurationProperties('app')
class ApplicationConfigurationProperties implements ApplicationConfiguration {

    @NonNull
    String hostedDomain
}
