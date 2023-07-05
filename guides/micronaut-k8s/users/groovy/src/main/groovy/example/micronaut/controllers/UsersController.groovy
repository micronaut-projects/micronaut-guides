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

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

@Controller("/users") // <1>
@Secured(SecurityRule.IS_AUTHENTICATED) // <2>
class UsersController {

    List<User> persons = []

    @Post // <3>
    User add(@Body @Valid User user) {
        def existingUser = findByUsername(user.username)

        if (existingUser.isPresent()) {
            throw new HttpStatusException(HttpStatus.CONFLICT, "User with provided username already exists");
        }

        def newUser = new User(persons.size() + 1, user.firstName, user.lastName, user.username)
        persons.add(newUser)
        newUser
    }

    @Get("/{id}") // <4>
    User findById(int id) {
        persons.stream()
                .filter(it -> it.id == id)
                .findFirst().orElse(null)
    }

    @Get // <5>
    List<User> getUsers() {
        persons
    }

    Optional<User> findByUsername(@NotNull String username) {
        persons.stream()
                .filter(it -> it.username == username)
                .findFirst()
    }
}
