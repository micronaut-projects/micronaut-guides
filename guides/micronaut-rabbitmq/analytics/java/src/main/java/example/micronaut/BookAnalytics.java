package example.micronaut;

import io.micronaut.core.annotation.Introspected;

@Introspected
public class BookAnalytics {

    private String bookIsbn;
    private Long count;

    public BookAnalytics() {
    }

    public BookAnalytics(String bookIsbn, Long count) {
        this.bookIsbn = bookIsbn;
        this.count = count;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
