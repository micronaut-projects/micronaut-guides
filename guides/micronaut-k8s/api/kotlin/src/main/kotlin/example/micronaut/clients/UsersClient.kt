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
    fun getById(id: Int): User

    @Post("/users")
    fun createUser(@Body user: User?): User

    @get:Get("/users")
    val users: List<User>
}