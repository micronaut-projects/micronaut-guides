from jakarta.inject import Singleton
from micronaut.http import HttpRequest, HttpResponse, HttpStatus
from micronaut.http.annotation import Produces
from micronaut.http.server.exceptions import ExceptionHandler
from micronaut.http.server.exceptions.response import ErrorContext, ErrorResponseProcessor
from micronaut.transaction.exceptions import OracleTransactionPriorityException


@Produces
@Singleton
class TransactionPriorityExceptionHandler(
        ExceptionHandler[OracleTransactionPriorityException, HttpResponse]):  # <1>

    def __init__(self, error_response_processor: ErrorResponseProcessor):
        self.error_response_processor = error_response_processor

    def handle(self, request: HttpRequest, exception: OracleTransactionPriorityException) -> HttpResponse:
        error_context = (ErrorContext.builder(request)
                         .cause(exception)
                         .errorMessage("Oracle rolled back this operation in favor of a higher-priority transaction")  # <2>
                         .build())
        return self.error_response_processor.processResponse(
            error_context, HttpResponse.status(HttpStatus.CONFLICT))  # <3>
