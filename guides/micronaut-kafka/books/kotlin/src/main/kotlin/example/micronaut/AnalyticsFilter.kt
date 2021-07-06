package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import io.reactivex.Flowable
import org.reactivestreams.Publisher

@Filter("/books/?*")
class AnalyticsFilter(private val analyticsClient: AnalyticsClient) : HttpServerFilter {

    override fun doFilter(request: HttpRequest<*>,
                          chain: ServerFilterChain): Publisher<MutableHttpResponse<*>> =

            Flowable.fromPublisher(chain.proceed(request))
                    .switchMap { response: MutableHttpResponse<*> ->
                        val book = response.getBody(Book::class.java)
                        book.ifPresent { b: Book -> analyticsClient.updateAnalytics(b) }
                        Flowable.just(response)
                    }
}
