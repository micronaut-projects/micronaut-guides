package example.micronaut

import io.micronaut.core.annotation.Nullable
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.oauth2.endpoint.authorization.state.State
import io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

@Named("github") // <1>
@Singleton
class GithubAuthenticationMapper(private val apiClient: GithubApiClient) : OauthAuthenticationMapper {

    override fun createAuthenticationResponse(tokenResponse: TokenResponse,
                                              @Nullable state: State): Publisher<AuthenticationResponse> =
        Mono.from(apiClient.getUser(TOKEN_PREFIX + tokenResponse.accessToken)) // <2>
                .map { (login): GithubUser -> AuthenticationResponse.success(login,
                        listOf(ROLE_GITHUB),
                        mapOf(OauthAuthenticationMapper.ACCESS_TOKEN_KEY to tokenResponse.accessToken)) // <3>
                }

    companion object {
        const val TOKEN_PREFIX = "token "
        const val ROLE_GITHUB = "ROLE_GITHUB"
    }
}
