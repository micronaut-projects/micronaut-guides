package example.micronaut
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class Book(var isbn: String, val name: String)