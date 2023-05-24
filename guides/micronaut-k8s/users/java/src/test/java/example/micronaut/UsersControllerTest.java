package example.micronaut;

import example.micronaut.auth.Credentials;
import example.micronaut.models.User;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest // <1>
class UsersControllerTest {

    @Inject
    UsersClient usersClient;

    @Inject
    Credentials credentials;

    @Test
    void testUnauthorized() {
        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> usersClient.getUsers(""));
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatus());
    }

    @Test
    void getUserThatDoesntExists() {
        String authHeader = basicAuth(credentials);
        User retriedUser = usersClient.getById(authHeader, 100);
        assertNull(retriedUser);
    }

    @Test
    void multipleUserInteraction() {
        String authHeader = basicAuth(credentials);

        String firstName = "firstName";
        String lastName = "lastName";
        String username = "username";

        User user = new User(0 ,firstName, lastName, username);

        User createdUser = usersClient.createUser(authHeader, user);

        assertEquals(firstName, createdUser.firstName());
        assertEquals(lastName, createdUser.lastName());
        assertEquals(username, createdUser.username());
        assertNotNull(createdUser.id());

        User retriedUser = usersClient.getById(authHeader, createdUser.id());

        assertEquals(firstName, retriedUser.firstName());
        assertEquals(lastName, retriedUser.lastName());
        assertEquals(username, retriedUser.username());

        List<User> users = usersClient.getUsers(authHeader);
        assertNotNull(users);
        assertTrue(users.stream()
                .map(User::username)
                .anyMatch(name -> name.equals(username)));

    }

    @Test
    void createSameUserTwice() {
        String authHeader = basicAuth(credentials);

        String firstName = "SameUserFirstName";
        String lastName = "SameUserLastName";
        String username = "SameUserUsername";

        User user = new User(0 ,firstName, lastName, username);

        User createdUser = usersClient.createUser(authHeader, user);

        assertEquals(firstName, createdUser.firstName());
        assertEquals(lastName, createdUser.lastName());
        assertEquals(username, createdUser.username());
        assertNotNull(createdUser.id());
        assertNotEquals(createdUser.id(), 0);

        HttpClientResponseException exception = assertThrows(HttpClientResponseException.class, () -> usersClient.createUser(authHeader, user));
        assertEquals(HttpStatus.CONFLICT, exception.getStatus());
        assertTrue(exception.getResponse().getBody(String.class).orElse("").contains("User with provided username already exists"));

    }
    private static String basicAuth(Credentials credentials) {
        return basicAuth(credentials.username(), credentials.password());
    }
    private static String basicAuth(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }
}
