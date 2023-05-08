package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.retry.annotation.Fallback;
import org.reactivestreams.Publisher;
import jakarta.inject.Singleton;
import jakarta.validation.constraints.NotBlank;
import reactor.core.publisher.Mono;

@Requires(env = Environment.TEST)
@Fallback
@Singleton
public class BookInventoryClientStub implements BookInventoryOperations {

    @Override
    public Mono<Boolean> stock(@NotBlank String isbn) {
        if (isbn.equals("1491950358")) {
            return Mono.just(Boolean.TRUE);

        } else if (isbn.equals("1680502395")) {
            return Mono.just(Boolean.FALSE);
        }
        return Mono.empty();
    }
}
