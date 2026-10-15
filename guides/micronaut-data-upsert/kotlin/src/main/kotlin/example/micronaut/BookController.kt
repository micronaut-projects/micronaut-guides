package example.micronaut

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status

@Controller("/books") // <1>
class BookController(private val bookRepository: BookRepository) { // <2>

    @Post // <3>
    @Status(HttpStatus.CREATED) // <4>
    fun save(@Body book: Book) { // <5>
        bookRepository.upsert(book) // <6>
    }

    @Put // <7>
    @Status(HttpStatus.OK)
    fun update(@Body book: Book) {
        bookRepository.upsert(book) // <6>
    }
}
