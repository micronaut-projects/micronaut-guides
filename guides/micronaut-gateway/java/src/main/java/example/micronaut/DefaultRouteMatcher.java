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
            for (Predicate predicate : route.getPredicates()) {
                if (matches(predicate, request)) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("route {} matched", route.getName());
                    }
                    return Optional.of(route);
                }
            }
        }
        return Optional.empty();
    }

    private boolean matches(@NonNull Route route, @NonNull HttpRequest<?> request) {
        for (Predicate predicate : route.getPredicates()) {
            if (matches(predicate, request)) {
                return true;
            }
        }
        return false;
    }

    private boolean matches(@NonNull Predicate predicate, @NonNull HttpRequest<?> request) {
        if (predicate instanceof ConfigurationPredicate configurationPredicate) {
            if (configurationPredicate.getPath() != null) {
                return request.getPath().startsWith(configurationPredicate.getPath());
            }
        }
        return false;
    }
}
