package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.http.HttpRequest
import jakarta.inject.Singleton

@CompileStatic
@Singleton // <1>
class HttpHeaderColorFetcher implements ColorFetcher {

    @Override
    @NonNull
    Optional<String> favouriteColor(@NonNull HttpRequest<?> request) {
        return request.headers.get('color', String)
    }

    @Override
    int getOrder() { // <2>
        return 10
    }
}
