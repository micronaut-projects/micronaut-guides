package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.endpoints.TokenRefreshRequest;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
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
                new TokenRefreshRequest(rsp.getRefreshToken())), AccessRefreshToken.class); // <1>

        assertNotNull(refreshResponse.getAccessToken());
        assertNotEquals(rsp.getAccessToken(), refreshResponse.getAccessToken()); // <2>

        refreshTokenRepository.deleteAll();
    }
}
