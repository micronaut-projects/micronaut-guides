package example.micronaut

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class BookRequestHandlerTest {

    @Test
    fun testHandler() {
        val bookRequestHandler = BookRequestHandler()
        val book = Book()
        book.name = "Building Microservices"
        val bookSaved = bookRequestHandler.execute(book)
        assertNotNull(bookSaved)
        assertEquals(book.name, bookSaved!!.name)
        assertNotNull(bookSaved.isbn)
        bookRequestHandler.applicationContext.close()
    }
}
