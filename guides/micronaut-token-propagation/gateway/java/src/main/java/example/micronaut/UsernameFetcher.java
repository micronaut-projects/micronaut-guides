package example.micronaut;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

public interface UsernameFetcher {
    Mono<String> findUsername();
}
