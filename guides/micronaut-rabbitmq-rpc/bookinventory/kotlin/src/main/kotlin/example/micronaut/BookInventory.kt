package example.micronaut

import io.micronaut.core.annotation.Introspected

@Introspected
data class BookInventory(val isbn: String, val stock: Int)
