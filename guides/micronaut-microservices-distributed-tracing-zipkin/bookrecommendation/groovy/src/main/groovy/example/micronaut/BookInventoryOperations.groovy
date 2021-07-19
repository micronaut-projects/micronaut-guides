package example.micronaut

import io.reactivex.Maybe
import javax.validation.constraints.NotBlank

interface BookInventoryOperations {
    Maybe<Boolean> stock(@NotBlank String isbn)
}
