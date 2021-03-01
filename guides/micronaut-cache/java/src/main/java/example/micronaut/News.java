package example.micronaut;

import io.micronaut.core.annotation.Introspected;

import java.time.Month;
import java.util.List;

@Introspected
public class News {
    private Month month;

    private List<String> headlines;

    public News() {

    }

    public News(Month month, List<String> headlines) {
        this.month = month;
        this.headlines = headlines;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    public List<String> getHeadlines() {
        return headlines;
    }

    public void setHeadlines(List<String> headlines) {
        this.headlines = headlines;
    }
}
