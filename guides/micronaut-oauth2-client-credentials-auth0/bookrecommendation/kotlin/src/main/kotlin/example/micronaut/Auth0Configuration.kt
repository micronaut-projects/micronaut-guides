package example.micronaut

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("auth0") // <1>
interface Auth0Configuration {
    fun getApiIdentifier(): String
}