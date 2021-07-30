package example.micronaut;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.util.List;

@MicronautTest
class AnalyticsServiceTest {

    @Inject
    AnalyticsService analyticsService;

    @Test
    void testUpdateBookAnalyticsAndGetAnalytics() {
        Book b1 = new Book("1491950358", "Building Microservices");
        Book b2 = new Book("1680502395", "Release It!");

        analyticsService.updateBookAnalytics(b1);
        analyticsService.updateBookAnalytics(b1);
        analyticsService.updateBookAnalytics(b1);
        analyticsService.updateBookAnalytics(b2);

        List<BookAnalytics> analytics = analyticsService.listAnalytics();
        assertEquals(2, analytics.size());

        assertEquals(3, findBookAnalytics(b1, analytics).getCount());
        assertEquals(1, findBookAnalytics(b2, analytics).getCount());
    }

    private BookAnalytics findBookAnalytics(Book b, List<BookAnalytics> analytics) {
        return analytics
                .stream()
                .filter(bookAnalytics -> bookAnalytics.getBookIsbn().equals(b.getIsbn()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }
}
