package example.micronaut;

import example.micronaut.auth.Credentials;
import example.micronaut.models.User;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientException;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class UsersControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    Credentials credentials;

    @Test
    void testUnauthorized() {
        HttpClientException exception = assertThrows(HttpClientException.class, () -> client.toBlocking().retrieve(HttpRequest.GET("/users"), HttpStatus.class));
        assertTrue(exception.getMessage().contains("Unauthorized"));
    }

    @Test
    void getUsers() {
        HttpStatus status = client.toBlocking().retrieve(
                HttpRequest.GET("/users")
                        .basicAuth(credentials.getUsername(), credentials.getPassword())
                , HttpStatus.class);
        assertEquals(HttpStatus.OK, status);
    }

    @Test
    void multipleUserInteraction() {
        User user = new User();
        String firstName = "firstName";
        String lastName = "lastName";
        String username = "username";

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        User createdUser = client.toBlocking().retrieve(
                HttpRequest.POST("/users", user)
                        .basicAuth(
                                credentials.getUsername(), credentials.getPassword()
                        ), User.class
        );
        assertEquals(firstName, createdUser.getFirstName());
        assertEquals(lastName, createdUser.getLastName());
        assertEquals(username, createdUser.getUsername());
        assertNotNull(createdUser.getId());

        User retriedUser = client.toBlocking().retrieve(
                HttpRequest.GET("/users/" + createdUser.getId())
                        .basicAuth(credentials.getUsername(), credentials.getPassword())
                , User.class
        );
        assertEquals(firstName, retriedUser.getFirstName());
        assertEquals(lastName, retriedUser.getLastName());
        assertEquals(username, retriedUser.getUsername());

        HttpResponse<List<User>> rsp = client.toBlocking().exchange(
                HttpRequest.GET("/users")
                        .basicAuth(credentials.getUsername(), credentials.getPassword()),
                Argument.listOf(User.class));

        assertEquals(HttpStatus.OK, rsp.getStatus());
        assertNotNull(rsp.body());
        List<User> users = rsp.body();
        assertNotNull(users);
        assertTrue(users.stream()
                .map(User::getUsername)
                .anyMatch(name -> name.equals(username)));

    }

    @Test
    void createSameUserTwice() {
        User user = new User();
        String firstName = "SameUserFirstName";
        String lastName = "SameUserLastName";
        String username = "SameUserUsername";

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        User createdUser = client.toBlocking().retrieve(
                HttpRequest.POST("/users", user)
                        .basicAuth(
                                credentials.getUsername(), credentials.getPassword()
                        ), User.class
        );
        assertEquals(firstName, createdUser.getFirstName());
        assertEquals(lastName, createdUser.getLastName());
        assertEquals(username, createdUser.getUsername());
        assertNotNull(createdUser.getId());

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> client.toBlocking().retrieve(
                HttpRequest.POST("/users", user)
                        .basicAuth(
                                credentials.getUsername(), credentials.getPassword()
                        ), User.class
        ));
        assertEquals(exception.getStatus(), HttpStatus.CONFLICT);
        assertTrue(exception.getResponse().getBody(String.class).orElse("").contains("User with provided username already exists"));

    }
}
