package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;

import java.util.Optional;

@FunctionalInterface
public interface RouteMatcher {

    @NonNull
    Optional<Route> matches(@NonNull HttpRequest<?> request);
}
