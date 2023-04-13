package example.micronaut

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class BookInventory(val isbn: String, val stock: Int)
