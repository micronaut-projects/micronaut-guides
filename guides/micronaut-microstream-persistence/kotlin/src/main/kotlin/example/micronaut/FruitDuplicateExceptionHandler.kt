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
class FruitDuplicateExceptionHandler(private val errorResponseProcessor: ErrorResponseProcessor<*>) :
    ExceptionHandler<FruitDuplicateException, HttpResponse<*>> {

    override fun handle(request: HttpRequest<*>, exception: FruitDuplicateException): HttpResponse<*> {
        val errorContext = ErrorContext.builder(request)
            .cause(exception)
            .errorMessage(exception.message ?: "No message")
            .build()
        return errorResponseProcessor.processResponse(errorContext, HttpResponse.unprocessableEntity<Any>())
    }
}