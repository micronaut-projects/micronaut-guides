package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import jakarta.inject.Singleton

@Singleton // <1>
class ErrorExceptionHandler implements ExceptionHandler<HttpClientResponseException, HttpResponse<?>> {

    private final ErrorResponseProcessor<?> errorResponseProcessor;

    ErrorExceptionHandler(ErrorResponseProcessor<?> errorResponseProcessor) {
        this.errorResponseProcessor = errorResponseProcessor;
    }

    @Override
    HttpResponse handle(HttpRequest request, HttpClientResponseException e) {
        errorResponseProcessor.processResponse(ErrorContext.builder(request)
                .cause(e)
                .errorMessage(e.response.getBody(String.class).orElse(null))
                .build(), HttpResponse.status(e.status));
    }
}
