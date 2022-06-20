package example.micronaut;

import io.micronaut.http.annotation.Header;
import reactor.core.publisher.Mono;

public interface UsernameFetcher {
    Mono<String> findUsername(@Header("Authorization") String authorization);
}
