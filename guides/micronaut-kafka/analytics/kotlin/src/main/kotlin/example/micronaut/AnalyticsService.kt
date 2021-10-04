package example.micronaut

import java.util.concurrent.ConcurrentHashMap
import jakarta.inject.Singleton

@Singleton
class AnalyticsService {

    private val bookAnalytics: MutableMap<Book, Long> = ConcurrentHashMap() // <1>

    fun updateBookAnalytics(book: Book) { // <2>
        bookAnalytics.compute(book) { k, v ->
            if (v == null) return@compute 1L else return@compute v + 1
        }
    }

    fun listAnalytics(): List<BookAnalytics> = // <3>
            bookAnalytics.entries.map { (key, value) -> BookAnalytics(key.isbn, value) }
}
