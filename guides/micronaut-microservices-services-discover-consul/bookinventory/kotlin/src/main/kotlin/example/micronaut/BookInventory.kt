package example.micronaut

import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotBlank

@Serdeable
data class BookInventory(@NotBlank val isbn: String,
                         val stock: Int)
