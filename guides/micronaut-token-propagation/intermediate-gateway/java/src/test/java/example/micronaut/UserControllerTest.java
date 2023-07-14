package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static io.micronaut.http.HttpStatus.UNAUTHORIZED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest // <1>
class UserControllerTest {

    @Inject
    @Client("/")
    HttpClient client; // <2>

    @Test
    void testUserEndpointIsSecured() { // <3>
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange(HttpRequest.GET("/user"));
        });

        assertEquals(UNAUTHORIZED, thrown.getResponse().getStatus());
    }

    @Test
    void testAuthenticatedCanFetchUsername() {
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("sherlock", "password");
        HttpRequest<?> request = HttpRequest.POST("/login", credentials);

        BearerAccessRefreshToken bearerAccessRefreshToken = client.toBlocking().retrieve(request, BearerAccessRefreshToken.class);

        String username = client.toBlocking().retrieve(HttpRequest.GET("/user")
                .header("Authorization", "Bearer " + bearerAccessRefreshToken.getAccessToken()), String.class);

        assertEquals("sherlock", username);
    }
}
