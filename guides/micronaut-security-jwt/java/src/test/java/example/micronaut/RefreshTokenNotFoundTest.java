package example.micronaut;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.UserDetails;
import io.micronaut.security.token.generator.RefreshTokenGenerator;
import io.micronaut.security.token.jwt.endpoints.TokenRefreshRequest;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
class RefreshTokenNotFoundTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Inject
    RefreshTokenGenerator refreshTokenGenerator;

    @Test
    void accessingSecuredURLWithoutAuthenticatingReturnsUnauthorized() {
        UserDetails user = new UserDetails("sherlock", Collections.emptyList());

        String refreshToken = refreshTokenGenerator.createKey(user);
        Optional<String> refreshTokenOptional = refreshTokenGenerator.generate(user, refreshToken);
        assertTrue(refreshTokenOptional.isPresent());

        String signedRefreshToken = refreshTokenOptional.get();  // <1>
        Argument<BearerAccessRefreshToken> bodyArgument = Argument.of(BearerAccessRefreshToken.class);
        Argument<Map> errorArgument = Argument.of(Map.class);
        HttpRequest req = HttpRequest.POST("/oauth/access_token", new TokenRefreshRequest(signedRefreshToken));

        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(req, bodyArgument, errorArgument);
        });
        assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());

        Optional<Map> mapOptional = e.getResponse().getBody(Map.class);
        assertTrue(mapOptional.isPresent());

        Map m = mapOptional.get();
        assertEquals("invalid_grant", m.get("error"));
        assertEquals("refresh token not found", m.get("error_description"));
    }
}