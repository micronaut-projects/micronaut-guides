package example.micronaut;

import io.micronaut.http.HttpRequest;

import java.util.function.Predicate;

public interface RoutePredicate extends Predicate<HttpRequest<?>> {
}
