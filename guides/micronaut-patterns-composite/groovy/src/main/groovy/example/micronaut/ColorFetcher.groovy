package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.core.order.Ordered
import io.micronaut.http.HttpRequest

@FunctionalInterface // <1>
interface ColorFetcher extends Ordered { // <2>

    @NonNull
    Optional<String> favouriteColor(@NonNull HttpRequest<?> request)
}
