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
import java.util.Map

@Named('auth0') // <1>
@Replaces(ClientCredentialsClient) // <2>
@Singleton // <3>
class Auth0ClientCredentials extends DefaultClientCredentialsClient { // <4>
    private final Auth0Configuration auth0Configuration

    Auth0ClientCredentials(@Named('auth0') OauthClientConfiguration oauthClientConfiguration,  // <5>
                                  TokenEndpointClient tokenEndpointClient,
                                  Auth0Configuration auth0Configuration) {
        super(oauthClientConfiguration, tokenEndpointClient)
        this.auth0Configuration = auth0Configuration
    }


    @Override
    protected ClientCredentialsTokenRequestContext createTokenRequestContext(@Nullable String scope) {
        new ClientCredentialsTokenRequestContextExtension(scope, oauthClientConfiguration, auth0Configuration)
    }

    static class ClientCredentialsTokenRequestContextExtension extends ClientCredentialsTokenRequestContext {
        private final Auth0Configuration auth0Configuration
        public ClientCredentialsTokenRequestContextExtension(String scope,
                                                    OauthClientConfiguration clientConfiguration,
                                                    Auth0Configuration auth0Configuration) {
            super(scope, clientConfiguration)
            this.auth0Configuration = auth0Configuration
        }
        
        @Override
        public Map<String, String> getGrant() {
            Map<String, String> m = super.grant
            m['audience'] = auth0Configuration.apiIdentifier // <6>
            m
        }
    }
}