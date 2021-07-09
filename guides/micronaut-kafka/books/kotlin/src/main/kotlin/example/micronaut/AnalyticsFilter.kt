package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import io.reactivex.Flowable
import org.reactivestreams.Publisher

@Filter("/books/?*") // <1>
class AnalyticsFilter(private val analyticsClient: AnalyticsClient) // <3>
    : HttpServerFilter { // <2>

    override fun doFilter(request: HttpRequest<*>, // <4>
                          chain: ServerFilterChain): Publisher<MutableHttpResponse<*>> =

            Flowable.fromPublisher(chain.proceed(request)) // <5>
                    .switchMap { response: MutableHttpResponse<*> ->
                        val book = response.getBody(Book::class.java) // <6>
                        book.ifPresent { b: Book -> analyticsClient.updateAnalytics(b) } // <7>
                        Flowable.just(response)
                    }
}
