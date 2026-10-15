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
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.rules.SecurityRuleResult;
import jakarta.inject.Singleton;
import org.reactivestreams.Publisher;

import java.util.Optional;

@Singleton
class RouteSecurityRule<B> implements SecurityRule<HttpRequest<B>> {

    private final RouteMatcher routeMatcher;

    RouteSecurityRule(RouteMatcher routeMatcher) {
        this.routeMatcher = routeMatcher;
    }

    @Override
    public Publisher<SecurityRuleResult> check(@Nullable HttpRequest<B> request, @Nullable Authentication authentication) {
        return Publishers.just(securityRuleResult(request, authentication));
    }

    @NonNull
    private SecurityRuleResult securityRuleResult(@Nullable HttpRequest<?> request, @Nullable Authentication authentication) {
        Optional<Route> routeOptional = routeMatcher.matches(request);
        if (routeOptional.isPresent()) {
            Route route = routeOptional.get();
            if (authentication == null && route.getRolesAllowed().contains(SecurityRule.IS_ANONYMOUS)) {
                return SecurityRuleResult.ALLOWED;
            }
            if (authentication != null && authentication.getRoles()
                    .stream()
                    .anyMatch(role -> route.getRolesAllowed().contains(role))) {
                return SecurityRuleResult.ALLOWED;
            }
        }
        return SecurityRuleResult.UNKNOWN;
    }
}
