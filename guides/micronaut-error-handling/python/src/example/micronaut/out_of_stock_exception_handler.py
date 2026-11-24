from jakarta.inject import Singleton
from micronaut.context.annotation import Requires
from micronaut.http import HttpRequest, HttpResponse
from micronaut.http.annotation import Produces
from micronaut.http.server.exceptions import ExceptionHandler

from .out_of_stock_exception import OutOfStockException


# tag::clazz[]
@Produces
@Singleton  # <1>
@Requires(classes=[OutOfStockException, ExceptionHandler])  # <2>
class OutOfStockExceptionHandler(ExceptionHandler[OutOfStockException, HttpResponse]):  # <3>
    def handle(self, request: HttpRequest, exception: OutOfStockException) -> HttpResponse:
        return HttpResponse.ok(0)  # <4>
# end::clazz[]
