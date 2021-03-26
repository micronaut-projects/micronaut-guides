package example.micronaut
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions

class BookRequestHandlerTest {

    @Test
    fun testHandler() {
        val bookRequestHandler = BookRequestHandler()
        val book = Book()
        book.name = "Building Microservices"
        val bookSaved = bookRequestHandler.execute(book)
        Assertions.assertNotNull(bookSaved)
        Assertions.assertEquals(book.name, bookSaved!!.name)
        Assertions.assertNotNull(bookSaved.isbn)
        bookRequestHandler.applicationContext.close()
    }
}