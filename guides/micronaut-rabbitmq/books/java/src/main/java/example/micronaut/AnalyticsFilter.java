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

    private final AnalyticsClient analyticsClient; // <3>

    public AnalyticsFilter(AnalyticsClient analyticsClient) { // <3>
        this.analyticsClient = analyticsClient;
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) { // <4>
        return Flux
                .from(chain.proceed(request)) // <5>
                .flatMap(response ->
                        Flux.fromCallable(() -> {
                            Optional<Book> book = response.getBody(Book.class); // <6>
                            book.ifPresent(analyticsClient::updateAnalytics); // <7>

                            return response;
                        })
                );
    }
}
