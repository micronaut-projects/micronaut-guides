package example.micronaut

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected

@Introspected
data class BookInventory @Creator constructor(val isbn: String, val stock: Int)
