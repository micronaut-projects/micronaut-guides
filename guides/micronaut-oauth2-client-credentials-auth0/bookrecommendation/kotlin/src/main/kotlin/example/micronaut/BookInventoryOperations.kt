package example.micronaut

import reactor.core.publisher.Mono
import javax.validation.constraints.NotBlank

interface BookInventoryOperations {
    fun stock(@NotBlank isbn: String): Mono<Boolean>
}
