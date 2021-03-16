package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class UserControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    public void testUserEndpointIsSecured() {
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/user"));
        });

        assertEquals(HttpStatus.UNAUTHORIZED, thrown.getResponse().getStatus());
    }

    @Test
    public void testAuthenticatedCanFetchUsername() {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("sherlock", "password");
        HttpRequest request = HttpRequest.POST("/login", credentials);

        BearerAccessRefreshToken bearerAccessRefreshToken = client.toBlocking().retrieve(request, BearerAccessRefreshToken.class);

        String username = client.toBlocking().retrieve(HttpRequest.GET("/user")
                .header("Authorization", "Bearer " + bearerAccessRefreshToken.getAccessToken()), String.class);

        assertEquals("sherlock", username);
    }
}