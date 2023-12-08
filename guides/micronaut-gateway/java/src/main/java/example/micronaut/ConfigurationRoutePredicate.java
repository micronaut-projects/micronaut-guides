package example.micronaut;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;

public interface ConfigurationRoutePredicate extends RoutePredicate {

    @Nullable
    String getPath();

    @Override
    default boolean test(HttpRequest<?> request) {
        if (getPath() != null) {
            return request.getPath().equals(getPath());
        }
        return false;
    }
}
