package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment.TEST
import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Mono

@Client(id = "userecho") // <1>
@Requires(notEnv = [TEST]) // <2>
interface UserEchoClient : UsernameFetcher {

    @Consumes(TEXT_PLAIN)
    @Get("/user") // <3>
    override fun findUsername(@Header("Authorization") authorization: String): Mono<String> // <4>
}
