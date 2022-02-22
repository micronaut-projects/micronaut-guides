package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton

@Produces
@Singleton // <1>
@Requires(classes = [OutOfStockException::class, ExceptionHandler::class]) // <2>
class OutOfStockExceptionHandler : ExceptionHandler<OutOfStockException, HttpResponse<*>> { // <3>

    override fun handle(request: HttpRequest<*>, exception: OutOfStockException): HttpResponse<*> =
            HttpResponse.ok(0) // <4>
}
