package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.oauth2.endpoint.authorization.state.State;
import io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Named("github") // <1>
@Singleton
public class GithubAuthenticationMapper implements OauthAuthenticationMapper {

    public static final String TOKEN_PREFIX = "token ";
    public static final String ROLE_GITHUB = "ROLE_GITHUB";

    private final GithubApiClient apiClient;

    public GithubAuthenticationMapper(GithubApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse,
                                                                          @Nullable State state) {
        return Mono.from(apiClient.getUser(TOKEN_PREFIX + tokenResponse.getAccessToken())) // <2>
                .map(user -> AuthenticationResponse.success(user.getLogin(),
                        Collections.singletonList(ROLE_GITHUB),
                        Collections.singletonMap(ACCESS_TOKEN_KEY, tokenResponse.getAccessToken()))); // <3>
    }
}
