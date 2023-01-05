package example.micronaut.domain

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class Book(
    var isbn: String,
    var name: String,
    var genre: Genre?) {

    var id: Long? = null

    override fun toString() = "Book{id=$id, name='$name', isbn='$isbn', genre=$genre}"
}
