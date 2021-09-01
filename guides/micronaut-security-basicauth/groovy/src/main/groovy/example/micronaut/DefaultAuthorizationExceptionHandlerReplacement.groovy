package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.security.authentication.AuthorizationException
import io.micronaut.security.authentication.DefaultAuthorizationExceptionHandler
import jakarta.inject.Singleton

import static io.micronaut.http.HttpHeaders.WWW_AUTHENTICATE
import static io.micronaut.http.HttpStatus.FORBIDDEN
import static io.micronaut.http.HttpStatus.UNAUTHORIZED

@Singleton // <1>
@Replaces(DefaultAuthorizationExceptionHandler) // <2>
class DefaultAuthorizationExceptionHandlerReplacement extends DefaultAuthorizationExceptionHandler {

    @Override
    protected MutableHttpResponse<?> httpResponseWithStatus(HttpRequest request,
                                                            AuthorizationException e) {
        if (e.isForbidden()) {
            return HttpResponse.status(FORBIDDEN)
        }
        HttpResponse.status(UNAUTHORIZED)
                .header(WWW_AUTHENTICATE, 'Basic realm="Micronaut Guide"')
    }
}
