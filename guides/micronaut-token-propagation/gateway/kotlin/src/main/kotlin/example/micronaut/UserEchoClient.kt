package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment.TEST
import io.micronaut.http.MediaType.TEXT_PLAIN
import io.micronaut.http.annotation.Consumes
import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Mono

@Client(id = "userecho")
@Requires(notEnv = [TEST])
interface UserEchoClient : UsernameFetcher {

    @Consumes(TEXT_PLAIN)
    @Get("/user")
    override fun findUsername(): Mono<String>
}
