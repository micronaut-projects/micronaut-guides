package example.micronaut

import example.micronaut.auth.Credentials
import example.micronaut.models.User
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientException
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import java.util.Base64


@MicronautTest
class UsersControllerTest {
    @Inject
    var usersClient: UsersClient? = null

    @Inject
    var credentials: Credentials? = null

    @Test
    fun testUnauthorized() {
        val exception = Assertions.assertThrows(
            HttpClientException::class.java
        ) { usersClient!!.getUsers("") }
        Assertions.assertTrue(exception.message!!.contains("Unauthorized"))
    }

    @Test
    fun userThatDoesntExists() {
        val authHeader = "Basic " + Base64.getEncoder()
            .encodeToString((credentials!!.username + ":" + credentials!!.password).toByteArray())
        val retriedUser = usersClient!!.getById(authHeader, 100)
        Assertions.assertNull(retriedUser)
    }

    @Test
    fun multipleUserInteraction() {
        val authHeader = "Basic " + Base64.getEncoder()
            .encodeToString((credentials!!.username + ":" + credentials!!.password).toByteArray())
        val firstName = "firstName"
        val lastName = "lastName"
        val username = "username"
        val user = User(0, firstName, lastName, username)
        val createdUser = usersClient!!.createUser(authHeader, user)
        assertEquals(firstName, createdUser.firstName)
        assertEquals(lastName, createdUser.lastName)
        assertEquals(username, createdUser.username)
        Assertions.assertNotNull(createdUser.id)
        val retriedUser = usersClient!!.getById(authHeader, createdUser.id)
        assertEquals(firstName, retriedUser!!.firstName)
        assertEquals(lastName, retriedUser.lastName)
        assertEquals(username, retriedUser.username)
        val users = usersClient!!.getUsers(authHeader)
        Assertions.assertNotNull(users)
        Assertions.assertNotNull(users.any {
            it!!.username == username
        })
    }

    @Test
    fun createSameUserTwice() {
        val authHeader = "Basic " + Base64.getEncoder()
            .encodeToString((credentials!!.username + ":" + credentials!!.password).toByteArray())
        val firstName = "SameUserFirstName"
        val lastName = "SameUserLastName"
        val username = "SameUserUsername"
        val user = User(0, firstName, lastName, username)
        val createdUser = usersClient!!.createUser(authHeader, user)
        assertEquals(firstName, createdUser.firstName)
        assertEquals(lastName, createdUser.lastName)
        assertEquals(username, createdUser.username)
        Assertions.assertNotNull(createdUser.id)
        assertNotEquals(createdUser.id, 0)
        val exception = Assertions.assertThrows(
            HttpClientResponseException::class.java
        ) { usersClient!!.createUser(authHeader, user) }
        Assertions.assertEquals(exception.status, HttpStatus.CONFLICT)
        Assertions.assertTrue(
            exception.response.getBody(String::class.java).orElse("")
                .contains("User with provided username already exists")
        )
    }
}