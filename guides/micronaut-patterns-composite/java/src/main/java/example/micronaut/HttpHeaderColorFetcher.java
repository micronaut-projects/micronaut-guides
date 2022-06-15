package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton // <1>
public class HttpHeaderColorFetcher implements ColorFetcher {
    @Override
    @NonNull
    public Optional<String> favouriteColor(@NonNull HttpRequest<?> request) {
        return request.getHeaders().get("color", String.class);
    }

    @Override
    public int getOrder() { // <2>
        return 10;
    }
}
