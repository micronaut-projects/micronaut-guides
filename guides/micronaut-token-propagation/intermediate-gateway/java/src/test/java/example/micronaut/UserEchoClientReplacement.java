package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.http.annotation.Header;
import org.reactivestreams.Publisher;
import io.micronaut.core.async.annotation.SingleResult;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Requires(env = Environment.TEST)
@Singleton
public class UserEchoClientReplacement implements UsernameFetcher {

    @Override
    @SingleResult
    public Publisher<String> findUsername(@Header("Authorization") String authorization) {
        return Mono.just("sherlock");
    }
}
