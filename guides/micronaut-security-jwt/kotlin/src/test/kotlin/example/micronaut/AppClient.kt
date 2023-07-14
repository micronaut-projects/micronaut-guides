package example.micronaut

import io.micronaut.http.HttpHeaders.AUTHORIZATION
import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken

@Client("/")
interface AppClient {

    @Post("/login")
    fun login(@Body credentials: UsernamePasswordCredentials): BearerAccessRefreshToken

    @Consumes(TEXT_PLAIN)
    @Get
    fun home(@Header(AUTHORIZATION) authorization: String): String
}
