package example.micronaut

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class BookInventory @Creator constructor(
        @NotBlank val isbn: String,
        val stock: Int)
