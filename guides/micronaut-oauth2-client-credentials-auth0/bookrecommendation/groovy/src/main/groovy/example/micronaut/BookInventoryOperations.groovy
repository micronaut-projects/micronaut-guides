package example.micronaut

import io.micronaut.core.annotation.NonNull
import reactor.core.publisher.Mono

import javax.validation.constraints.NotBlank

interface BookInventoryOperations {
    Mono<Boolean> stock(@NonNull @NotBlank String isbn)
}
