package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Requires
import io.micronaut.http.annotation.Header
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

import static io.micronaut.context.env.Environment.TEST

@CompileStatic
@Requires(env = TEST)
@Singleton
class UserEchoClientReplacement implements UsernameFetcher {

    @Override
    Mono<String> findUsername(@Header('Authorization') String authorization) {
        return Mono.just('sherlock')
    }
}
