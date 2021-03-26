package example.micronaut
import io.micronaut.core.annotation.Introspected
import io.micronaut.function.aws.MicronautRequestHandler
import java.util.UUID

@Introspected
class BookRequestHandler : MicronautRequestHandler<Book?, BookSaved?>() {

    override fun execute(input: Book?): BookSaved? {
        return if (input != null) {
            val bookSaved = BookSaved()
            bookSaved.name = input.name
            bookSaved.isbn = UUID.randomUUID().toString()
            return bookSaved
        } else {
            null
        }
    }
}