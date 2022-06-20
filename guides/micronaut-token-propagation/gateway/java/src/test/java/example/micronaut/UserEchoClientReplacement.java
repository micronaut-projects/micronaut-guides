package example.micronaut;

import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

import static io.micronaut.context.env.Environment.TEST;

@Requires(env = TEST)
@Singleton
public class UserEchoClientReplacement implements UsernameFetcher {

    @Override
    public Mono<String> findUsername() {
        return Mono.just("sherlock");
    }
}
