package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn

@CompileStatic
@Controller('/books') // <1>
@ExecuteOn(TaskExecutors.IO) // <2>
class BookController {

    private final BookRepository bookRepository

    BookController(BookRepository bookRepository) { // <3>
        this.bookRepository = bookRepository
    }

    @Get// <4>
    List<Book> index() {
        return bookRepository.findAll()
    }

    @Get('/{isbn}') // <5>
    Optional<Book> findBook(String isbn) {
        return bookRepository.findByIsbn(isbn)
    }
}
