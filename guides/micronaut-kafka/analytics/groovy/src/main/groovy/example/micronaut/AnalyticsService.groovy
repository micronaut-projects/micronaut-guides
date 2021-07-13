package example.micronaut

import groovy.transform.CompileStatic

import javax.inject.Singleton
import java.util.concurrent.ConcurrentHashMap

@CompileStatic
@Singleton
class AnalyticsService {

    private final Map<Book, Long> bookAnalytics = new ConcurrentHashMap<>() // <1>

    void updateBookAnalytics(Book book) { // <2>
        bookAnalytics.compute(book, (k, v) -> {
            v == null ? 1L : v + 1
        })
    }

    List<BookAnalytics> listAnalytics() { // <3>
        bookAnalytics.collect { e -> new BookAnalytics(e.key.isbn, e.value) }
    }
}
