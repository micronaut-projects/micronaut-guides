package example.micronaut;

import io.micronaut.serde.annotation.Serdeable;

import java.time.Month;
import java.util.List;

@Serdeable // <1>
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
