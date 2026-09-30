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

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Property(name = "micronaut.gateway.routes.micronaut.uri", value = "https://micronaut.io")
@Property(name = "micronaut.gateway.routes.micronaut.predicates[0].path", value = "/micronaut/books")
@Property(name = "micronaut.gateway.routes.grails.uri", value = "https://grails.org")
@Property(name = "micronaut.gateway.routes.grails.predicates[0].path", value = "/micronaut/grails")
@MicronautTest(startApplication = false)
class RouteConfigurationTest {

    @Inject
    ApplicationContext context;

    @Test
    void configuration() {
        Collection<Route> routes = context.getBeansOfType(Route.class);
        Optional<List<Map<String, String>>> property = context.getProperty("micronaut.gateway.routes.micronaut.predicates", Argument.listOf(Argument.mapOf(String.class, String.class)));
        assertNotNull(property);
        assertTrue(property.isPresent());
        assertEquals(1, property.get().size());
        assertEquals("/micronaut/books", property.get().get(0).get("path"));
        assertEquals(2, routes.size());
        assertTrue(routes.stream().anyMatch(route -> route.getName().equals("micronaut")));
        assertTrue(routes.stream().anyMatch(route -> route.getName().equals("micronaut")));
        assertTrue(routes.stream().allMatch(route -> route.getPredicates() != null));
        assertTrue(routes.stream().allMatch(route -> route.getPredicates() != null && route.getPredicates().size() == 1));
        assertTrue(routes.stream().allMatch(route -> route.getPredicates() != null && route.getPredicates().size() == 1 && ((ConfigurationRoutePredicate) route.getPredicates().get(0)).getPath() != null));
    }
}