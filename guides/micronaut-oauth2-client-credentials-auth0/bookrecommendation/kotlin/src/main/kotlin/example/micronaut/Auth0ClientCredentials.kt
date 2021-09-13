package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.core.annotation.Nullable
import io.micronaut.security.oauth2.client.clientcredentials.ClientCredentialsClient
import io.micronaut.security.oauth2.client.clientcredentials.DefaultClientCredentialsClient
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration
import io.micronaut.security.oauth2.endpoint.token.request.TokenEndpointClient
import io.micronaut.security.oauth2.endpoint.token.request.context.ClientCredentialsTokenRequestContext
import jakarta.inject.Named
import jakarta.inject.Singleton

@Named("auth0") // <1>
@Replaces(ClientCredentialsClient::class) // <2>
@Singleton // <3>
internal class Auth0ClientCredentials(@Named("auth0") oauthClientConfiguration: OauthClientConfiguration,  // <5>
                                      tokenEndpointClient: TokenEndpointClient, private val auth0Configuration: Auth0Configuration)
    : DefaultClientCredentialsClient(oauthClientConfiguration, tokenEndpointClient) { // <4>
    override fun createTokenRequestContext(@Nullable scope: String): ClientCredentialsTokenRequestContext {
        return ClientCredentialsTokenRequestContextExtension(
            scope, oauthClientConfiguration,
            auth0Configuration
        )
    }

    internal class ClientCredentialsTokenRequestContextExtension(scope: String,
                                                                 clientConfiguration: OauthClientConfiguration,
                                                                 private val auth0Configuration: Auth0Configuration)
        : ClientCredentialsTokenRequestContext(scope, clientConfiguration) {
        override fun getGrant(): Map<String, String> {
            val m = super.getGrant()
            m["audience"] = auth0Configuration.getApiIdentifier() // <6>
            return m
        }
    }
}