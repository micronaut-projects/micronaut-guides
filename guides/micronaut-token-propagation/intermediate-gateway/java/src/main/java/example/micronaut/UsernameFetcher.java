package example.micronaut;

import io.micronaut.http.annotation.Header;
import org.reactivestreams.Publisher;
import io.micronaut.core.async.annotation.SingleResult;

public interface UsernameFetcher {
    @SingleResult
    Publisher<String> findUsername(@Header("Authorization") String authorization);
}
