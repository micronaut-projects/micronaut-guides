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