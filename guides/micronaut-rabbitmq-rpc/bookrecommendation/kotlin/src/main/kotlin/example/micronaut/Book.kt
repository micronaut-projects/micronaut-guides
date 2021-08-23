package example.micronaut

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected

@Introspected
data class Book @Creator constructor(val isbn: String, val name: String)
