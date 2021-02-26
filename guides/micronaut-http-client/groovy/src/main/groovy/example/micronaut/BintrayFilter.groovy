package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.ClientFilterChain
import io.micronaut.http.filter.HttpClientFilter
import org.reactivestreams.Publisher

@CompileStatic
@Filter('/api/${bintray.apiversion}/repos/**') // <1>
@Requires(property = "bintray.username") // <2>
@Requires(property = "bintray.token") // <2>
class BintrayFilter  implements HttpClientFilter {

    private final BintrayConfiguration configuration

    BintrayFilter(BintrayConfiguration configuration ) { // <3>
        this.configuration = configuration
    }

    @Override
    Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
        return chain.proceed(request.basicAuth(configuration.username, configuration.token)) // <4>
    }
}
