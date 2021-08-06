package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import io.reactivex.Flowable
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
        return Flowable
                .fromPublisher(chain.proceed(request)) // <5>
                .flatMap(response -> {
                    Book book = response.getBody(Book).orElse(null) // <6>
                    if (book) {
                        analyticsClient.updateAnalytics(book).map(b -> response).toFlowable() // <7>
                    }
                    else {
                        Flowable.just response
                    }
                })
    }
}
