package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import io.micronaut.http.server.exceptions.response.ErrorContext
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import jakarta.inject.Singleton

@Produces // <1>
@Singleton // <2>
class FruitDuplicateExceptionHandler implements ExceptionHandler<FruitDuplicateException, HttpResponse<?>> {

    private final ErrorResponseProcessor<?> errorResponseProcessor

    FruitDuplicateExceptionHandler(ErrorResponseProcessor<?> errorResponseProcessor) {
        this.errorResponseProcessor = errorResponseProcessor
    }

    @Override
    HttpResponse<?> handle(HttpRequest request, FruitDuplicateException exception) {
        ErrorContext errorContext = ErrorContext.builder(request)
                .cause(exception)
                .errorMessage(exception.getMessage())
                .build()
        return errorResponseProcessor.processResponse(errorContext, HttpResponse.badRequest())
    }
}
