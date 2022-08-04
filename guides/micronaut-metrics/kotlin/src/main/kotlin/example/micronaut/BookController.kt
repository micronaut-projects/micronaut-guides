package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micrometer.core.annotation.Timed
import io.micrometer.core.annotation.Counted

import java.util.Optional

@Controller("/books") // <1>
@ExecuteOn(TaskExecutors.IO) // <2>
open class BookController(private val bookRepository: BookRepository) { // <3>

    @Get// <4>
    @Timed("books.index") // <5>
    open fun index(): Iterable<Book> = bookRepository.findAll()

    @Get("/{isbn}") // <6>
    @Counted("books.find") // <7>
    open fun findBook(isbn: String): Optional<Book> = bookRepository.findByIsbn(isbn)
}
