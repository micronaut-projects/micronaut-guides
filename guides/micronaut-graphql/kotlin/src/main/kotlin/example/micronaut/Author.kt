package example.micronaut

import io.micronaut.core.annotation.Introspected

@Introspected
class Author(val id: String, val firstName: String, val lastName: String)
