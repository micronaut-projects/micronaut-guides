package example.micronaut;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.token.jwt.endpoints.TokenRefreshRequest;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static io.micronaut.http.HttpStatus.BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class UnsignedRefreshTokenTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void accessingSecuredURLWithoutAuthenticatingReturnsUnauthorized() {

        String unsignedRefreshedToken = "foo"; // <1>

        Argument<BearerAccessRefreshToken> bodyArgument = Argument.of(BearerAccessRefreshToken.class);
        Argument<Map> errorArgument = Argument.of(Map.class);

        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(
                    HttpRequest.POST("/oauth/access_token", new TokenRefreshRequest(unsignedRefreshedToken)),
                    bodyArgument,
                    errorArgument);
        });
        assertEquals(BAD_REQUEST, e.getStatus());

        Optional<Map> mapOptional = e.getResponse().getBody(Map.class);
        assertTrue(mapOptional.isPresent());

        Map m = mapOptional.get();
        assertEquals("invalid_grant", m.get("error"));
        assertEquals("Refresh token is invalid", m.get("error_description"));
    }
}
