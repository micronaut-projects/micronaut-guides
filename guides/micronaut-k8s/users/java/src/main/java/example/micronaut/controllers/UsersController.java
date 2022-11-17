package example.micronaut.controller;

import example.micronaut.models.User;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Controller("/users") // <1>
@Secured(SecurityRule.IS_AUTHENTICATED) // <2>
@Validated
public class UsersController {

    List<User> persons = new ArrayList<>();

    @Post // <3>
    public Mono<Object> add(@Body @Valid User user) {
        return Mono.from(findByUsername(user.getUsername()))
                .flatMap((userv) -> Mono.error(new HttpStatusException(HttpStatus.CONFLICT, "User with provided username already exists")))
                .switchIfEmpty(Mono.defer(() -> {
                    user.setId(persons.size() + 1);
                    persons.add(user);
                    return Mono.just(user);
                }));
    }

    @Get("/{id}") // <4>
    public Mono<User> findById(@NotNull Integer id) {
        return Mono.justOrEmpty(persons.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst());
    }

    @Get // <5>
    public Flux<User> getUsers() {
        return Flux.fromStream(persons.stream());
    }

    public Mono<User> findByUsername(@NotNull String username) {
        return Mono.justOrEmpty(persons.stream()
                .filter(it -> it.getUsername().equals(username))
                .findFirst());
    }

}
