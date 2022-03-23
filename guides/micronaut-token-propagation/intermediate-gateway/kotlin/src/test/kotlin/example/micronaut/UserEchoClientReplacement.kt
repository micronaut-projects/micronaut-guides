package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment.TEST
import io.micronaut.http.annotation.Header
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

@Requires(env = [TEST])
@Singleton
class UserEchoClientReplacement : UsernameFetcher {

    override fun findUsername(@Header("Authorization") authorization: String): Mono<String> =
            Mono.just("sherlock")
}
