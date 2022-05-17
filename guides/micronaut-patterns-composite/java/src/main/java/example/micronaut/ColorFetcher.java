package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.order.Ordered;
import io.micronaut.http.HttpRequest;

import java.util.Optional;

@FunctionalInterface // <1>
public interface ColorFetcher extends Ordered { // <2>

    @NonNull
    Optional<String> favouriteColor(@NonNull HttpRequest<?> request);
}
