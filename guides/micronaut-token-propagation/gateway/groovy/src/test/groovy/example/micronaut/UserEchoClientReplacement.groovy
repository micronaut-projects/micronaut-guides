package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Requires
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

import static io.micronaut.context.env.Environment.TEST

@CompileStatic
@Requires(env = TEST)
@Singleton
class UserEchoClientReplacement implements UsernameFetcher {

    @Override
    Mono<String> findUsername() {
        return Mono.just('sherlock')
    }
}
