package example.micronaut

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.core.annotation.NonNull

@ConfigurationProperties("auth0") // <1>
interface Auth0Configuration {
    @NonNull
    String getApiIdentifier()
}