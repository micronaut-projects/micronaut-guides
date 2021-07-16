package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.http.HttpHeaderValues;
import io.micronaut.security.authentication.AuthenticationFailed;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.oauth2.endpoint.authorization.state.State;
import io.micronaut.security.oauth2.endpoint.token.response.OauthUserDetailsMapper;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import reactor.core.publisher.Mono;
import org.reactivestreams.Publisher;

import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.Collections;
import java.util.Map;

@Named("linkedin")
@Singleton
public class LinkedInOauthUserDetailsMapper implements OauthUserDetailsMapper {

    private final LinkedInApiClient linkedInApiClient;

    public LinkedInOauthUserDetailsMapper(LinkedInApiClient linkedInApiClient) {
        this.linkedInApiClient = linkedInApiClient;
    }

    @Override
    public Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse,
                                                                          @Nullable State state) {
        return Mono.from(linkedInApiClient.me(HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER + " " + tokenResponse.getAccessToken()))
                .map(linkedMe -> {
                    Map<String, Object> attributes = CollectionUtils.mapOf("firstName", linkedMe.getLocalizedFirstName(),
                            "lastName", linkedMe.getLocalizedLastName());
                    String username = linkedMe.getId();
                    return new UserDetails(username, Collections.emptyList(), attributes);
                });
    }
}
