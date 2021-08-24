package example.micronaut

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.oauth2.endpoint.authorization.state.State
import io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

@Named("linkedin")
@Singleton
class LinkedInOauthAuthenticationMapper(private val linkedInApiClient: LinkedInApiClient) : OauthAuthenticationMapper {

    override fun createAuthenticationResponse(tokenResponse: TokenResponse,
                                              @Nullable state: State): Publisher<AuthenticationResponse> =

        Mono.from(linkedInApiClient.me(AUTHORIZATION_PREFIX_BEARER + ' ' + tokenResponse.accessToken))
                .map { (username, localizedFirstName, localizedLastName): LinkedInMe ->
                    val attributes = mapOf(
                            "firstName" to localizedFirstName,
                            "lastName" to localizedLastName)
                    AuthenticationResponse.success(username, emptyList(), attributes)
                }
}
