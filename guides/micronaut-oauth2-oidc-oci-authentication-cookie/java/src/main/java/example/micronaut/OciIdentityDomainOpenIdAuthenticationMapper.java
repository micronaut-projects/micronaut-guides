package example.micronaut;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.core.util.StringUtils;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.oauth2.endpoint.authorization.state.State;
import io.micronaut.security.oauth2.endpoint.token.response.DefaultOpenIdAuthenticationMapper;
import io.micronaut.security.oauth2.endpoint.token.response.OpenIdAuthenticationMapper;
import io.micronaut.security.oauth2.endpoint.token.response.OpenIdClaims;
import io.micronaut.security.oauth2.endpoint.token.response.OpenIdTokenResponse;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Singleton // <1>
@Replaces(DefaultOpenIdAuthenticationMapper.class)
public class OciIdentityDomainOpenIdAuthenticationMapper implements OpenIdAuthenticationMapper {
    private static final String KEY_GROUPS = "groups";
    private static final String KEY_NAME = "name";

    @Override
    @NonNull
    public Publisher<AuthenticationResponse> createAuthenticationResponse(String providerName,
                                                                          OpenIdTokenResponse tokenResponse,
                                                                          OpenIdClaims openIdClaims,
                                                                          @Nullable State state) {
        return Publishers.just(authenticationResponse(openIdClaims));
    }

    private AuthenticationResponse authenticationResponse(OpenIdClaims openIdClaims) {
        List<String> roles = resolveRoles(openIdClaims);
        String subject = openIdClaims.getSubject();
        return AuthenticationResponse.success(subject, roles);
    }

    @NonNull
    private List<String> resolveRoles(@Nullable OpenIdClaims claims) {
        if (claims.get(KEY_GROUPS) == null) {
            return Collections.emptyList();
        }
        Object obj = claims.get(KEY_GROUPS);
        if (obj == null) {
            return Collections.emptyList();
        }
        if (obj instanceof Iterable objIterable) {
            List<String> roles = new ArrayList<>();
            for(Object o : objIterable) {
                if (o instanceof Map m) {
                    Object name = m.get(KEY_NAME);
                    if (name != null) {
                        String nameValue = name.toString();
                        if (StringUtils.isNotEmpty(nameValue)) {
                            roles.add(nameValue);
                        }
                    }
                }
            }
            return roles;
        }
        return Collections.emptyList();
    }
}
