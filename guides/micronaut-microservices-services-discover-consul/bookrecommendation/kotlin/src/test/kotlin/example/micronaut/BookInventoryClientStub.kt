package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment.TEST
import io.micronaut.retry.annotation.Fallback
import jakarta.inject.Singleton
import reactor.core.publisher.Mono
import jakarta.validation.constraints.NotBlank

@Requires(env = [TEST]) // <1>
@Fallback
@Singleton
open class BookInventoryClientStub : BookInventoryOperations {

    override fun stock(@NotBlank isbn: String): Mono<Boolean> {
        if (isbn == "1491950358") {
            return Mono.just(true) // <2>
        }
        if (isbn == "1680502395") {
            return Mono.just(false) // <3>
        }
        return Mono.empty() // <4>
    }
}
