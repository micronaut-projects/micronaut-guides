package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.retry.annotation.Fallback;
import org.reactivestreams.Publisher;
import io.micronaut.core.async.annotation.SingleResult;
import jakarta.inject.Singleton;
import javax.validation.constraints.NotBlank;
import reactor.core.publisher.Mono;

@Requires(env = Environment.TEST) // <1>
@Fallback
@Singleton
public class BookInventoryClientStub implements BookInventoryOperations {

    @Override
    @SingleResult
    public Publisher<Boolean> stock(@NonNull @NotBlank String isbn) {
        if (isbn.equals("1491950358")) {
            return Mono.just(Boolean.TRUE); // <2>

        } else if (isbn.equals("1680502395")) {
            return Mono.just(Boolean.FALSE); // <3>
        }
        return Mono.empty(); // <4>
    }
}
