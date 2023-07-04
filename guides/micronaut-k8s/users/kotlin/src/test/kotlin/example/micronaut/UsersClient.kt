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
    fun getById(@Header authorization: String?, id: Int?): User?

    @Post("/users")
    fun createUser(@Header authorization: String?, @Body user: User?): User

    @Get("/users")
    fun getUsers(@Header authorization: String?): List<User?>
}