package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import java.util.Optional

@Controller("/books") // <1>
@ExecuteOn(TaskExecutors.IO) // <2>
class BookController(private val bookRepository: BookRepository) { // <3>

    @Get// <4>
    fun index(): List<Book> = bookRepository.findAll()

    @Get("/{isbn}") // <5>
    fun findBook(isbn: String): Optional<Book> = bookRepository.findByIsbn(isbn)
}
