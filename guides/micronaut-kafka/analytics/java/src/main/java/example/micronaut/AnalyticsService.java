package example.micronaut;

import jakarta.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Singleton
public class AnalyticsService {

    private final Map<Book, Long> bookAnalytics = new ConcurrentHashMap<>(); // <1>

    public void updateBookAnalytics(Book book) { // <2>
        bookAnalytics.compute(book, (k, v) -> {
            return v == null ? 1L : v + 1;
        });
    }

    public List<BookAnalytics> listAnalytics() { // <3>
        return bookAnalytics
                .entrySet()
                .stream()
                .map(e -> new BookAnalytics(e.getKey().getIsbn(), e.getValue()))
                .collect(Collectors.toList());
    }
}
