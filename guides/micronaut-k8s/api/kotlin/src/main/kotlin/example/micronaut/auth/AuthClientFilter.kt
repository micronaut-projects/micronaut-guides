package example.micronaut.auth

import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpRequest
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.ClientFilterChain
import io.micronaut.http.filter.HttpClientFilter
import org.reactivestreams.Publisher

@Filter(Filter.MATCH_ALL_PATTERN)
class AuthClientFilter(credentials: Credentials) : HttpClientFilter {
    private val credentials: Credentials

    init {
        this.credentials = credentials
    }

    override fun doFilter(request: MutableHttpRequest<*>, chain: ClientFilterChain): Publisher<out HttpResponse<*>?> {
        return chain.proceed(request.basicAuth(credentials.username, credentials.password))
    }
}
