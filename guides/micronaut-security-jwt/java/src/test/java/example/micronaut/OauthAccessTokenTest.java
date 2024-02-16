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

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.endpoints.TokenRefreshRequest;
import io.micronaut.security.token.render.AccessRefreshToken;
import io.micronaut.security.token.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest(rollback = false)
class OauthAccessTokenTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    RefreshTokenRepository refreshTokenRepository;

    @Test
    void verifyJWTAccessTokenRefreshWorks() throws InterruptedException {
        String username = "sherlock";

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, "password");
        HttpRequest<?> request = HttpRequest.POST("/login", creds);

        long oldTokenCount = refreshTokenRepository.count();
        BearerAccessRefreshToken rsp = client.toBlocking().retrieve(request, BearerAccessRefreshToken.class);
        Thread.sleep(3_000);
        assertEquals(oldTokenCount + 1, refreshTokenRepository.count());

        assertNotNull(rsp.getAccessToken());
        assertNotNull(rsp.getRefreshToken());

        Thread.sleep(1_000); // sleep for one second to give time for the issued at `iat` Claim to change
        AccessRefreshToken refreshResponse = client.toBlocking().retrieve(HttpRequest.POST("/oauth/access_token",
                new TokenRefreshRequest(TokenRefreshRequest.GRANT_TYPE_REFRESH_TOKEN, rsp.getRefreshToken())), AccessRefreshToken.class); // <1>

        assertNotNull(refreshResponse.getAccessToken());
        assertNotEquals(rsp.getAccessToken(), refreshResponse.getAccessToken()); // <2>

        refreshTokenRepository.deleteAll();
    }
}
