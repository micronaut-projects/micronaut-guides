package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton

@Singleton // <1>
class ErrorExceptionHandler implements ExceptionHandler<HttpClientResponseException, HttpResponse<?>> {
    @Override
    HttpResponse<?> handle(HttpRequest request, HttpClientResponseException exception) {
        HttpResponse.status(exception.getResponse().status()).body(exception.response.getBody(String.class).orElse(null))
    }
}
