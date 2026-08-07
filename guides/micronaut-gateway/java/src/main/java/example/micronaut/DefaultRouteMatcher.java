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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Singleton
public class DefaultRouteMatcher implements RouteMatcher {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRouteMatcher.class);

    private final List<Route> routes;

    public DefaultRouteMatcher(List<Route> routes) {
        this.routes = routes;
    }

    @Override
    @NonNull
    public Optional<Route> matches(@NonNull HttpRequest<?> request) {
        for (Route route : routes) {
            for (RoutePredicate predicate : route.getPredicates()) {
                if (predicate.test(request)) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("route {} matched", route.getName());
                    }
                    return Optional.of(route);
                }
            }
        }
        return Optional.empty();
    }
}
