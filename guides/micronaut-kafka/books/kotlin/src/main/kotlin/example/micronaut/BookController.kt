package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.util.Optional

@Controller("/books") // <1>
class BookController(private val bookService: BookService) { // <2>

    @Get // <3>
    fun listAll(): List<Book> = bookService.listAll()

    @Get("/{isbn}") // <4>
    fun findBook(isbn: String): Optional<Book> = bookService.findByIsbn(isbn)
}
