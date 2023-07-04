package example.micronaut.controllers

import example.micronaut.models.User
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import javax.validation.Valid
import javax.validation.constraints.NotNull
import kotlin.collections.ArrayList

@Controller("/users") // <1>
@Secured(SecurityRule.IS_AUTHENTICATED) // <2>
class UsersController {
    var persons: MutableList<User?> = ArrayList()

    @Post // <3>
    fun add(@Body user: @Valid User?): User {
        val foundUser = findByUsername(user!!.username)
        if (foundUser != null) {
            throw HttpStatusException(HttpStatus.CONFLICT, "User with provided username already exists")
        }
        val newUser = User(persons.size + 1, user.firstName, user.lastName, user.username)
        persons.add(newUser)
        return newUser
    }

    @Get("/{id}") // <4>
    fun findById(id: @NotNull Int?): User? {
        return persons
            .firstOrNull { it: User? ->
                it!!.id == id
            }
    }

    @Get // <5>
    fun getUsers(): List<User?>? {
        return persons
    }

    fun findByUsername(username: @NotNull String?): User? {
        return persons.firstOrNull { it: User? ->
            it!!.username == username
        }
    }
}