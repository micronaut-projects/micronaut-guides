package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.Nullable
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.oauth2.endpoint.authorization.state.State
import io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

import static io.micronaut.http.HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER

@CompileStatic
@Named('linkedin')
@Singleton
class LinkedInOauthAuthenticationMapper implements OauthAuthenticationMapper {

    private final LinkedInApiClient linkedInApiClient

    LinkedInOauthAuthenticationMapper(LinkedInApiClient linkedInApiClient) {
        this.linkedInApiClient = linkedInApiClient
    }

    @Override
    Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse,
                                                                   @Nullable State state) {

        Mono.from(linkedInApiClient.me(AUTHORIZATION_PREFIX_BEARER + ' ' + tokenResponse.accessToken))
                .map(linkedMe -> {
                    Map<String, Object> attributes = [
                            'firstName': linkedMe.localizedFirstName,
                            'lastName': linkedMe.localizedLastName] as Map
                    String username = linkedMe.id
                    return AuthenticationResponse.success(username, Collections.emptyList(), attributes)
                })
    }
}
