package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.security.authentication.AuthorizationException
import io.micronaut.security.authentication.DefaultAuthorizationExceptionHandler
import javax.inject.Singleton

@Singleton // <1>
@Replaces(DefaultAuthorizationExceptionHandler) // <2>
class DefaultAuthorizationExceptionHandlerReplacement extends DefaultAuthorizationExceptionHandler {

    @Override
    protected MutableHttpResponse<?> httpResponseWithStatus(HttpRequest request, AuthorizationException exception) {
        if (exception.isForbidden()) {
            return HttpResponse.status(HttpStatus.FORBIDDEN)
        }
        HttpResponse.status(HttpStatus.UNAUTHORIZED)
                .header('WWW-Authenticate', 'Basic realm="Micronaut Guide"')
    }
}
