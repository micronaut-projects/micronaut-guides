package example.micronaut

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class Book(val isbn: String, val name: String)
