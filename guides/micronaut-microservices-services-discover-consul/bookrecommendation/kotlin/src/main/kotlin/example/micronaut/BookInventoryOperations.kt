package example.micronaut

import org.reactivestreams.Publisher
import io.micronaut.core.async.annotation.SingleResult
import javax.validation.constraints.NotBlank

interface BookInventoryOperations {
    @SingleResult
    fun stock(@NotBlank isbn: String): Publisher<Boolean>
}
