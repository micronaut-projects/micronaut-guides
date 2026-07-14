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

import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "micronaut.gateway.routes.micronaut.uri", value = "https://micronaut.io")
@Property(name = "micronaut.gateway.routes.micronaut.predicates[0].path", value = "/micronaut/books")
@Property(name = "micronaut.gateway.routes.grails.uri", value = "https://grails.org")
@Property(name = "micronaut.gateway.routes.grails.predicates[0].path", value = "/micronaut/grails")
@MicronautTest(startApplication = false)
class RouteMatcherTest {

    @Test
    void testRouteMatching(RouteMatcher routeMatcher) {
        assertTrue(routeMatcher.matches(HttpRequest.GET("/foo")).isEmpty());
        assertTrue(routeMatcher.matches(HttpRequest.GET("/micronaut/books")).isPresent());
    }

}