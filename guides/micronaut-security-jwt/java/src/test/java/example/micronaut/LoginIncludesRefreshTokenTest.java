package example.micronaut;

import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class LoginIncludesRefreshTokenTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void uponSuccessfulAuthenticationUserGetsAccessTokenAndRefreshToken() throws ParseException {
        UsernamePasswordCredentials creds = new UsernamePasswordCredentials("sherlock", "password");
        HttpRequest<?> request = HttpRequest.POST("/login", creds);
        BearerAccessRefreshToken rsp = client.toBlocking().retrieve(request, BearerAccessRefreshToken.class);

        assertEquals("sherlock", rsp.getUsername());
        assertNotNull(rsp.getAccessToken());
        assertNotNull(rsp.getRefreshToken()); // <1>

        assertTrue(JWTParser.parse(rsp.getAccessToken()) instanceof SignedJWT);
    }
}
