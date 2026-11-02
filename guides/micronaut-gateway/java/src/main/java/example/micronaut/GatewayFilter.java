/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.annotation.ServerFilter;
import io.micronaut.http.client.ProxyHttpClient;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.micronaut.http.filter.ServerFilterPhase;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.filters.SecurityFilter;
import org.reactivestreams.Publisher;
import java.net.URI;

@Requires(missingProperty = "framework")
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

    @Override
    public int getOrder() {
        return ServerFilterPhase.SECURITY.order() + 100;
    }
}
