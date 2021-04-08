package example.micronaut;

import edu.umd.cs.findbugs.annotations.Nullable;
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
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.Map;

@Named("linkedin")
@Singleton
public class LinkedInOauthUserDetailsMapper implements OauthUserDetailsMapper {

    private final LinkedInApiClient linkedInApiClient;

    public LinkedInOauthUserDetailsMapper(LinkedInApiClient linkedInApiClient) {
        this.linkedInApiClient = linkedInApiClient;
    }

    @Deprecated
    @Override
    public Publisher<UserDetails> createUserDetails(TokenResponse tokenResponse) {
        return null;
    }

    @Override
    public Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse,
                                                                          @Nullable State state) {
        return linkedInApiClient.me(HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER + " " + tokenResponse.getAccessToken())
                .map(linkedMe -> {
                    Map<String, Object> attributes = CollectionUtils.mapOf("firstName", linkedMe.getLocalizedFirstName(),
                            "lastName", linkedMe.getLocalizedLastName());
                    String username = linkedMe.getId();
                    return new UserDetails(username, Collections.emptyList(), attributes);
                });
    }
}
