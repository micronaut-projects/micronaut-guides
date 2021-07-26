package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import reactor.core.publisher.Flux
import org.reactivestreams.Publisher

@Filter("/books/?*") // <1>
class AnalyticsFilter(private val analyticsClient: AnalyticsClient) // <3>
    : HttpServerFilter { // <2>

    override fun doFilter(request: HttpRequest<*>, // <4>
                          chain: ServerFilterChain): Publisher<MutableHttpResponse<*>> =
        Flux
            .from(chain.proceed(request)) // <5>
            .flatMap { response: MutableHttpResponse<*> ->
                val book = response.getBody(Book::class.java).orElse(null) // <6>
                if (book == null) {
                    Flux.just(response)
                }
                else {
                    Flux.from(analyticsClient.updateAnalytics(book)).map { b -> response } // <7>
                }
            }
}
