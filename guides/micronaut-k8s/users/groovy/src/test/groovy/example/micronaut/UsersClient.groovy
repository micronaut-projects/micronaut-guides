package example.micronaut

import example.micronaut.models.User
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("/") // <1>
interface UsersClient {
    @Get("/users/{id}")
    User getById(@Header String authorization, int id)

    @Post("/users")
    User createUser(@Header String authorization, @Body User user)

    @Get("/users")
    List<User> getUsers(@Header String authorization)
}