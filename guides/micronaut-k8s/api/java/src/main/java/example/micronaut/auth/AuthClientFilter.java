package example.micronaut.auth;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.ClientFilterChain;
import io.micronaut.http.filter.HttpClientFilter;
import org.reactivestreams.Publisher;

@Filter(Filter.MATCH_ALL_PATTERN)
class AuthClientFilter implements HttpClientFilter {

    private final Credentials credentials;

    AuthClientFilter(Credentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public Publisher<? extends HttpResponse<?>> doFilter(MutableHttpRequest<?> request, ClientFilterChain chain) {
        return chain.proceed(request.basicAuth(credentials.username(), credentials.password()));
    }
}
