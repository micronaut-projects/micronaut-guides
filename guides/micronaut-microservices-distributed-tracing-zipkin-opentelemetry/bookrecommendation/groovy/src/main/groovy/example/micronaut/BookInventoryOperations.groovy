package example.micronaut

import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import jakarta.validation.constraints.NotBlank

interface BookInventoryOperations {
    Mono<Boolean> stock(@NotBlank String isbn)
}
