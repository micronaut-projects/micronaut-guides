package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton

@Singleton // <1>
class ErrorExceptionHandler :
    ExceptionHandler<HttpClientResponseException, HttpResponse<*>> {
    override fun handle(request: HttpRequest<*>?, exception: HttpClientResponseException): HttpResponse<*> {
        return HttpResponse.status<Any>(exception.response.status()).body(
            exception.response.getBody(
                String::class.java
            ).orElse(null)
        )
    }
}