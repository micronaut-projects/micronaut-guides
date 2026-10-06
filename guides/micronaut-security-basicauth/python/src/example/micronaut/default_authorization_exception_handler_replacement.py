from jakarta.inject import Singleton
from micronaut.context.annotation import Replaces
from micronaut.http import HttpHeaders, HttpResponse, HttpStatus, MutableHttpResponse
from micronaut.http.server.exceptions import ExceptionHandler
from micronaut.security.authentication import (
    AuthorizationException,
    DefaultAuthorizationExceptionHandler,
)


# tag::clazz[]
@Singleton  # <1>
@Replaces(DefaultAuthorizationExceptionHandler)  # <2>
class DefaultAuthorizationExceptionHandlerReplacement(
    ExceptionHandler[AuthorizationException, MutableHttpResponse]
):
    def handle(self, request, e: AuthorizationException) -> MutableHttpResponse:
        if e.isForbidden():
            return HttpResponse.status(HttpStatus.FORBIDDEN)
        return HttpResponse.status(HttpStatus.UNAUTHORIZED).header(
            HttpHeaders.WWW_AUTHENTICATE,
            'Basic realm="Micronaut Guide"',
        )
# end::clazz[]
