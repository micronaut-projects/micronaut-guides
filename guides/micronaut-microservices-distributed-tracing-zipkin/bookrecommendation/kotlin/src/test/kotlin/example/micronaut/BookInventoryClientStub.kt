package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.retry.annotation.Fallback
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import jakarta.inject.Singleton
import jakarta.validation.constraints.NotBlank

@Requires(env = arrayOf(Environment.TEST))
@Fallback
@Singleton
open class BookInventoryClientStub : BookInventoryOperations {

    override fun stock(@NotBlank isbn: String): Mono<Boolean> {
        if (isbn == "1491950358") {
            return Mono.just(java.lang.Boolean.TRUE)

        } else if (isbn == "1680502395") {
            return Mono.just(java.lang.Boolean.FALSE)
        }
        return Mono.empty()
    }
}
