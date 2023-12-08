package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.annotation.ServerFilter;
import io.micronaut.http.client.ProxyHttpClient;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.micronaut.http.uri.UriBuilder;
import org.reactivestreams.Publisher;
import java.net.URI;

@Filter(ServerFilter.MATCH_ALL_PATTERN)
class GatewayFilter implements HttpServerFilter {
    private final RouteMatcher routeMatcher;
    private final ProxyHttpClient proxyHttpClient;

    GatewayFilter(RouteMatcher routeMatcher,
                  ProxyHttpClient proxyHttpClient) {
        this.routeMatcher = routeMatcher;
        this.proxyHttpClient = proxyHttpClient;
    }

    @Override
    public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
        return routeMatcher.matches(request)
                .map(route -> proxyHttpClient.proxy(mutate(route, request)))
                .orElseGet(() -> chain.proceed(request));
    }

    @NonNull
    private MutableHttpRequest<?> mutate(@NonNull Route route, @NonNull HttpRequest<?> request) {
        MutableHttpRequest<?> mutableHttpRequest = request.mutate();
        URI uri = UriBuilder.of(route.getUri()).path(request.getPath()).build();
        mutableHttpRequest.uri(uri);
        return mutableHttpRequest;
    }
}
