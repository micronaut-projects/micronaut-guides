package example.micronaut

import groovy.transform.CompileStatic

import jakarta.annotation.PostConstruct
import jakarta.inject.Singleton

@CompileStatic
@Singleton
class BookService {

    private final List<Book> bookStore = []

    @PostConstruct
    void init() {
        bookStore << new Book('1491950358', 'Building Microservices')
        bookStore << new Book('1680502395', 'Release It!')
        bookStore << new Book('0321601912', 'Continuous Delivery')
    }

    List<Book> listAll() {
        bookStore
    }

    Optional<Book> findByIsbn(String isbn) {
        Optional.ofNullable(bookStore.find { it.isbn == isbn })
    }
}
