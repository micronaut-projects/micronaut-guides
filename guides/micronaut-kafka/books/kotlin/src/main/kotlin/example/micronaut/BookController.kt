package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.util.Optional

@Controller("/books")
class BookController(private val bookService: BookService) {

    @Get
    fun listAll(): List<Book> = bookService.listAll()

    @Get("/{isbn}")
    fun findBook(isbn: String): Optional<Book> = bookService.findByIsbn(isbn)
}
