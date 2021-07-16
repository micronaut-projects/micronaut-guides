package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import reactor.core.publisher.Flux;
import org.reactivestreams.Publisher;

import java.util.Optional;

@Filter("/books/?*") // <1>
public class AnalyticsFilter implements HttpServerFilter { // <2>

    private final AnalyticsClient analyticsClient;

    public AnalyticsFilter(AnalyticsClient analyticsClient) { // <3>
        this.analyticsClient = analyticsClient;
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request,
                                                      ServerFilterChain chain) { // <4>
        return Flux
                .from(chain.proceed(request)) // <5>
                .flatMap(response -> {
                    Book book = response.getBody(Book.class).orElse(null); // <6>
                    if (book == null) {
                        return Flux.just(response);
                    }
                    return Flux.from(analyticsClient.updateAnalytics(book)).map(b -> response); // <7>
                });
    }
}
