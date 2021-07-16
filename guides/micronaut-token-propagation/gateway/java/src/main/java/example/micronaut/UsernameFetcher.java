package example.micronaut;

import org.reactivestreams.Publisher;
import io.micronaut.core.async.annotation.SingleResult;

public interface UsernameFetcher {
    @SingleResult
    Publisher<String> findUsername();
}
