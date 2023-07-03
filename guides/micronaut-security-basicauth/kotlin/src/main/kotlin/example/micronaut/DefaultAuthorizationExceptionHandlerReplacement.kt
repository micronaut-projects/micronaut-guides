package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpHeaders.WWW_AUTHENTICATE
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.FORBIDDEN
import io.micronaut.http.HttpStatus.UNAUTHORIZED
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import io.micronaut.security.authentication.AuthorizationException
import io.micronaut.security.authentication.DefaultAuthorizationExceptionHandler
import io.micronaut.security.config.RedirectConfiguration
import io.micronaut.security.config.RedirectService
import io.micronaut.security.errors.PriorToLoginPersistence
import jakarta.inject.Singleton

@Singleton // <1>
@Replaces(DefaultAuthorizationExceptionHandler::class) // <2>
class DefaultAuthorizationExceptionHandlerReplacement(
    errorResponseProcessor: ErrorResponseProcessor<*>,
    redirectConfiguration: RedirectConfiguration,
    redirectService: RedirectService,
    priorToLoginPersistence: PriorToLoginPersistence<*, *>?
) : DefaultAuthorizationExceptionHandler(
    errorResponseProcessor, redirectConfiguration, redirectService, priorToLoginPersistence
) {

    override fun httpResponseWithStatus(
        request: HttpRequest<*>,
        e: AuthorizationException
    ): MutableHttpResponse<*> =
        if (e.isForbidden) {
            HttpResponse.status<Any>(FORBIDDEN);
        } else {
            HttpResponse.status<Any>(UNAUTHORIZED)
                .header(WWW_AUTHENTICATE, "Basic realm=\"Micronaut Guide\"")
        }
}
