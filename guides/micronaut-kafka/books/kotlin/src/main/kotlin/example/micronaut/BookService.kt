package example.micronaut

import java.util.Optional
import jakarta.annotation.PostConstruct
import jakarta.inject.Singleton
@Singleton
class BookService {

    private val bookStore: MutableList<Book> = mutableListOf()

    @PostConstruct
    fun init() {
        bookStore.add(Book("1491950358", "Building Microservices"))
        bookStore.add(Book("1680502395", "Release It!"))
        bookStore.add(Book("0321601912", "Continuous Delivery"))
    }

    fun listAll(): List<Book> = bookStore

    fun findByIsbn(isbn: String): Optional<Book> =
            bookStore.stream()
                    .filter { (i) -> i == isbn }
                    .findFirst()
}
