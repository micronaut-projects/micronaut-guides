package example.micronaut

import javax.inject.Singleton
import java.util.concurrent.ConcurrentHashMap

@Singleton
class AnalyticsService {

    private final Map<Book, Long> bookAnalytics = new ConcurrentHashMap<>() // <1>

    void updateBookAnalytics(Book book) { // <2>
        bookAnalytics.compute(book, (k, v) -> {
            if (v == null) {
                return 1L
            } else {
                return v + 1
            }
        })
    }

    List<BookAnalytics> listAnalytics() { // <3>
        return bookAnalytics.collect {e -> new BookAnalytics(e.key.isbn, e.value) }
    }
}
