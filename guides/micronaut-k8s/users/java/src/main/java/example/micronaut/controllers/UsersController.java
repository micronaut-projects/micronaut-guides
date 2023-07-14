package example.micronaut.controllers;

import example.micronaut.models.User;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller("/users") // <1>
@Secured(SecurityRule.IS_AUTHENTICATED) // <2>
class UsersController {
    List<User> persons = new ArrayList<>();

    @Post // <3>
    public User add(@Body @Valid User user) {
        Optional<User> existingUser = findByUsername(user.username());

        if (existingUser.isPresent()) {
            throw new HttpStatusException(HttpStatus.CONFLICT, "User with provided username already exists");
        }

        User newUser = new User(persons.size() + 1, user.firstName(), user.lastName(), user.username());
        persons.add(newUser);
        return newUser;
    }

    @Get("/{id}") // <4>
    public User findById(int id) {
        return persons.stream()
                .filter(it -> it.id().equals(id))
                .findFirst().orElse(null);
    }

    @Get // <5>
    public List<User> getUsers() {
        return persons;
    }

     Optional<User> findByUsername(@NotNull String username) {
        return persons.stream()
                .filter(it -> it.username().equals(username))
                .findFirst();
    }

}
