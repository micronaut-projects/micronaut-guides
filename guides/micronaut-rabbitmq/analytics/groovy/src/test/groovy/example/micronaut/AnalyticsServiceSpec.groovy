package example.micronaut

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest // <1>
class AnalyticsServiceSpec extends Specification {

    @Inject // <2>
    AnalyticsService analyticsService

    void 'test update book analytics and get analytics'() {
        given:
        Book b1 = new Book('1491950358', 'Building Microservices')
        Book b2 = new Book('1680502395', 'Release It!')

        when:
        analyticsService.updateBookAnalytics b1
        analyticsService.updateBookAnalytics b1
        analyticsService.updateBookAnalytics b1
        analyticsService.updateBookAnalytics b2

        List<BookAnalytics> analytics = analyticsService.listAnalytics()

        then:
        2 == analytics.size()
        3 == findBookAnalytics(b1, analytics).count
        1 == findBookAnalytics(b2, analytics).count
    }

    private BookAnalytics findBookAnalytics(Book b, List<BookAnalytics> analytics) {
        BookAnalytics bookAnalytics = analytics.find { it.bookIsbn == b.isbn }
        if (!bookAnalytics) {
            throw new RuntimeException('Book not found')
        }
        bookAnalytics
    }
}
