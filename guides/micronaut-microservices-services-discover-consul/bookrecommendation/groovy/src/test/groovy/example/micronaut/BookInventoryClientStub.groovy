package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import org.reactivestreams.Publisher
import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.retry.annotation.Fallback
import jakarta.inject.Singleton
import javax.validation.constraints.NotBlank
import reactor.core.publisher.Mono

@Requires(env = Environment.TEST) // <1>
@Fallback
@Singleton
class BookInventoryClientStub implements BookInventoryOperations {

    @Override
    @SingleResult
    Publisher<Boolean> stock(@NotBlank String isbn) {
        if (isbn == "1491950358") {
            return Mono.just(Boolean.TRUE) // <2>

        } else if (isbn == "1680502395") {
            return Mono.just(Boolean.FALSE) // <3>
        }
        Mono.empty() // <4>
    }
}
