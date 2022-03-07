package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment.TEST
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

@Requires(env = [TEST])
@Singleton
class UserEchoClientReplacement : UsernameFetcher {

    override fun findUsername(): Mono<String> = Mono.just("sherlock")
}
