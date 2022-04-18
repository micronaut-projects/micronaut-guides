package example.micronaut.controllers;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.uri.UriBuilder;

public abstract class ApplicationController {
    @NonNull
    protected HttpResponse<?> redirectTo(@NonNull CharSequence uri,
                                         @NonNull Long id) {
        return HttpResponse.seeOther(UriBuilder.of(uri)
                .path("" + id)
                .build());
    }
}
