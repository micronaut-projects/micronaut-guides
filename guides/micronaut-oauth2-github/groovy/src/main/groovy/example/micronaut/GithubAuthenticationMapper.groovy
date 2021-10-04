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

@CompileStatic
@Named('github') // <1>
@Singleton
class GithubAuthenticationMapper implements OauthAuthenticationMapper {

    public static final String TOKEN_PREFIX = 'token '
    public static final String ROLE_GITHUB = 'ROLE_GITHUB'

    private final GithubApiClient apiClient

    GithubAuthenticationMapper(GithubApiClient apiClient) {
        this.apiClient = apiClient
    }

    @Override
    Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse,
                                                                   @Nullable State state) {
        return Mono.from(apiClient.getUser(TOKEN_PREFIX + tokenResponse.accessToken)) // <2>
                .map(user -> AuthenticationResponse.success(user.login,
                        Collections.singletonList(ROLE_GITHUB),
                        Collections.singletonMap(ACCESS_TOKEN_KEY, tokenResponse.accessToken) as Map)) as Publisher // <3>
    }
}
