package example.micronaut.clients

import example.micronaut.models.User
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Client("users") // <1>
interface UsersClient {
    @Get("/users/{id}")
    Mono<User> getById(Integer id)

    @Post("/users")
    Mono<User> createUser(@Body User user)

    @Get("/users")
    Flux<User> getUsers()
}