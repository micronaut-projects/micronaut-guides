package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.retry.annotation.Fallback
import org.reactivestreams.Publisher
import io.micronaut.core.async.annotation.SingleResult
import jakarta.inject.Singleton
import javax.validation.constraints.NotBlank
import reactor.core.publisher.Mono

@Requires(env = Environment.TEST)
@Fallback
@Singleton
class BookInventoryClientStub implements BookInventoryOperations {

    @Override
    @SingleResult
    Publisher<Boolean> stock(@NotBlank String isbn) {
        if (isbn == "1491950358") {
            return Mono.just(Boolean.TRUE)

        } else if (isbn == "1680502395") {
            return Mono.just(Boolean.FALSE)
        }
        Mono.empty()
    }
}
