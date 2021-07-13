package example.micronaut;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class BookAnalytics {

    private final String bookIsbn;
    private final long count;

    @Creator
    public BookAnalytics(String bookIsbn, long count) {
        this.bookIsbn = bookIsbn;
        this.count = count;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public long getCount() {
        return count;
    }
}
