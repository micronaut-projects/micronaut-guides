package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import reactor.core.publisher.Flux
import org.reactivestreams.Publisher

@Filter('/books/?*') // <1>
class AnalyticsFilter implements HttpServerFilter { // <2>

    private final AnalyticsClient analyticsClient

    AnalyticsFilter(AnalyticsClient analyticsClient) { // <3>
        this.analyticsClient = analyticsClient
    }

    @Override
    Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request,
                                               ServerFilterChain chain) { // <4>
        return Flux
                .from(chain.proceed(request)) // <5>
                .flatMap(response -> {
                    Book book = response.getBody(Book).orElse(null) // <6>
                    if (book) {
                        Flux.from(analyticsClient.updateAnalytics(book)).map(b -> response) // <7>
                    }
                    else {
                        Flux.just response
                    }
                })
    }
}
