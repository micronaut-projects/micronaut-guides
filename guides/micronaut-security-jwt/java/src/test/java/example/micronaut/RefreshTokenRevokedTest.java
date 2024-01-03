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

import io.micronaut.context.ApplicationContext;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.token.generator.RefreshTokenGenerator;
import io.micronaut.security.endpoints.TokenRefreshRequest;
import io.micronaut.security.token.render.BearerAccessRefreshToken;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RefreshTokenRevokedTest {

    EmbeddedServer embeddedServer = ApplicationContext.run(EmbeddedServer.class, Collections.emptyMap());
    HttpClient client = embeddedServer.getApplicationContext().createBean(HttpClient.class, embeddedServer.getURL());
    RefreshTokenGenerator refreshTokenGenerator = embeddedServer.getApplicationContext().getBean(RefreshTokenGenerator.class);
    RefreshTokenRepository refreshTokenRepository = embeddedServer.getApplicationContext().getBean(RefreshTokenRepository.class);

    @Test
    void accessingSecuredURLWithoutAuthenticatingReturnsUnauthorized() {
        Authentication user = Authentication.build("sherlock");

        String refreshToken = refreshTokenGenerator.createKey(user);
        Optional<String> refreshTokenOptional = refreshTokenGenerator.generate(user, refreshToken);
        assertTrue(refreshTokenOptional.isPresent());

        long oldTokenCount = refreshTokenRepository.count();
        String signedRefreshToken = refreshTokenOptional.get();
        refreshTokenRepository.save(user.getName(), refreshToken, true); // <1>
        assertEquals(oldTokenCount + 1, refreshTokenRepository.count());

        Argument<BearerAccessRefreshToken> bodyArgument = Argument.of(BearerAccessRefreshToken.class);
        Argument<Map> errorArgument = Argument.of(Map.class);
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(
                    HttpRequest.POST("/oauth/access_token", new TokenRefreshRequest(TokenRefreshRequest.GRANT_TYPE_REFRESH_TOKEN, signedRefreshToken)),
                    bodyArgument,
                    errorArgument);
        });
        assertEquals(BAD_REQUEST, e.getStatus());

        Optional<Map> mapOptional = e.getResponse().getBody(Map.class);
        assertTrue(mapOptional.isPresent());

        Map m = mapOptional.get();
        assertEquals("invalid_grant", m.get("error"));
        assertEquals("refresh token revoked", m.get("error_description"));

        refreshTokenRepository.deleteAll();
    }
}
