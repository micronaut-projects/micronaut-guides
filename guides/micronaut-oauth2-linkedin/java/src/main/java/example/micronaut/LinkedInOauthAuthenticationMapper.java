/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.oauth2.endpoint.authorization.state.State;
import io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

import static io.micronaut.http.HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER;

@Named("linkedin") // <1>
@Singleton // <2>
public class LinkedInOauthAuthenticationMapper implements OauthAuthenticationMapper {

    private final LinkedInApiClient linkedInApiClient;

    public LinkedInOauthAuthenticationMapper(LinkedInApiClient linkedInApiClient) { // <3>
        this.linkedInApiClient = linkedInApiClient;
    }

    @Override
    public Publisher<AuthenticationResponse> createAuthenticationResponse(TokenResponse tokenResponse,
                                                                          @Nullable State state) {
        return Mono.from(linkedInApiClient.me(AUTHORIZATION_PREFIX_BEARER + ' ' + tokenResponse.getAccessToken()))
                .map(linkedMe -> {
                    Map<String, Object> attributes = CollectionUtils.mapOf(
                            "firstName", linkedMe.getLocalizedFirstName(),
                            "lastName", linkedMe.getLocalizedLastName());
                    String username = linkedMe.getId();
                    return AuthenticationResponse.success(username, Collections.emptyList(), attributes);
                });
    }
}
