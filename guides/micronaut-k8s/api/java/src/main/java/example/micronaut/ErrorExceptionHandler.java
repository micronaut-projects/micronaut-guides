package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Singleton // <1>
class ErrorExceptionHandler implements ExceptionHandler<HttpClientResponseException, HttpResponse<?>> {

    @Override
    public HttpResponse<?> handle(HttpRequest request, HttpClientResponseException exception) {
        return HttpResponse
                .status(exception.getResponse().status())
                .body(exception.getResponse().getBody(String.class).orElse(null));
    }
}
