from jakarta.inject import Singleton
from micronaut.data.exceptions import DataIntegrityViolationException
from micronaut.http import HttpRequest, HttpResponse, HttpStatus
from micronaut.http.annotation import Produces
from micronaut.http.server.exceptions import ExceptionHandler
from micronaut.http.server.exceptions.response import ErrorContext, ErrorResponseProcessor


@Produces
@Singleton
class ReservationExceptionHandler(
        ExceptionHandler[DataIntegrityViolationException, HttpResponse]):  # <1>

    def __init__(self, error_response_processor: ErrorResponseProcessor):
        self.error_response_processor = error_response_processor

    def handle(self, request: HttpRequest, exception: DataIntegrityViolationException) -> HttpResponse:
        error_context = (ErrorContext.builder(request)
                         .cause(exception)
                         .errorMessage("The operation violates an account constraint")  # <2>
                         .build())
        return self.error_response_processor.processResponse(
            error_context, HttpResponse.status(HttpStatus.CONFLICT))  # <3>
