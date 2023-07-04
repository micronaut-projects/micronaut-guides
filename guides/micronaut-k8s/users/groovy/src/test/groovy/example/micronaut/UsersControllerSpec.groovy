package example.micronaut

import example.micronaut.auth.Credentials
import example.micronaut.models.User
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.CONFLICT
import static io.micronaut.http.HttpStatus.UNAUTHORIZED

@MicronautTest // <1>
class UsersControllerSpec extends Specification {

    @Inject
    UsersClient usersClient

    @Inject
    Credentials credentials

    void "unauthorized"() {

        when:
        usersClient.getUsers("")

        then:
        HttpClientResponseException e = thrown()
        e.response.status == UNAUTHORIZED
        e.message.contains("Unauthorized")
    }

    void "get user that doesn't exist"() {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((credentials.username + ":" + credentials.password).getBytes())

        when:
        User retriedUser = usersClient.getById(authHeader, 100)

        then:
        retriedUser == null
    }

    void "multiple user interaction"() {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((credentials.username + ":" + credentials.password).getBytes())

        String firstName = "firstName"
        String lastName = "lastName"
        String username = "username"

        User user = new User(0 ,firstName, lastName, username)

        when:
        User createdUser = usersClient.createUser(authHeader, user)

        then:
        createdUser.firstName == firstName
        createdUser.lastName == lastName
        createdUser.username == username
        createdUser.id != 0

        when:
        User retriedUser = usersClient.getById(authHeader, createdUser.id)

        then:
        retriedUser.firstName == firstName
        retriedUser.lastName == lastName
        retriedUser.username == username
        retriedUser.id != 0

        when:
        List<User> users = usersClient.getUsers(authHeader)

        then:
        users != null
        users.stream().map(x->x.username).anyMatch(name -> name == username)
    }

    void "create same user twice"() {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((credentials.username + ":" + credentials.password).getBytes())

        String firstName = "SameUserFirstName"
        String lastName = "SameUserLastName"
        String username = "SameUserUsername"

        User user = new User(0 ,firstName, lastName, username)

        when:
        User createdUser = usersClient.createUser(authHeader, user)

        then:
        createdUser.firstName == firstName
        createdUser.firstName == firstName
        createdUser.lastName == lastName
        createdUser.username == username
        createdUser.id != 0

        when:
        usersClient.createUser(authHeader, user)

        then:
        HttpClientResponseException e = thrown()
        e.response.status == CONFLICT
        e.response.getBody(String.class).orElse("").contains("User with provided username already exists")
    }

}
