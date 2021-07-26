package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import org.reactivestreams.Publisher;
import io.micronaut.core.async.annotation.SingleResult;
import reactor.core.publisher.Mono;
import jakarta.inject.Singleton;

@Requires(env = Environment.TEST)
@Singleton
public class UserEchoClientReplacement implements UsernameFetcher {

    @Override
    @SingleResult
    public Publisher<String> findUsername() {
        return Mono.just("sherlock");
    }
}
