package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.security.authentication.AuthorizationException
import io.micronaut.security.authentication.DefaultAuthorizationExceptionHandler
import jakarta.inject.Singleton
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse

@Singleton // <1>
@Replaces(DefaultAuthorizationExceptionHandler::class)  // <2>
class DefaultAuthorizationExceptionHandlerReplacement : DefaultAuthorizationExceptionHandler() {
    override fun httpResponseWithStatus(
        request: HttpRequest<*>?,
        exception: AuthorizationException?
    ): MutableHttpResponse<*> {
        return if (exception?.isForbidden == true) {
            HttpResponse.status<Any>(HttpStatus.FORBIDDEN);
        } else HttpResponse.status<Any>(HttpStatus.UNAUTHORIZED)
            .header("WWW-Authenticate", "Basic realm=\"Micronaut Guide\"");
    }
}