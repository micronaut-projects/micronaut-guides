# tag::package[]
# end::package[]

# tag::imports[]
from typing import Annotated

from jakarta.validation import ConstraintViolationException, Valid
from micronaut.http import HttpResponse, HttpStatus, MediaType
from micronaut.http.annotation import Body, Consumes, Controller, Error, Get, Post, Produces, Status
from micronaut.views import View

from .command_book_save import CommandBookSave
from .message_source import MessageSource
from .out_of_stock_exception import OutOfStockException
# end::imports[]


# tag::clazz[]
@Controller("/books")  # <1>
class BookController:
# end::clazz[]

    # tag::di[]
    def __init__(self, message_source: MessageSource):  # <1>
        self.message_source = message_source
    # end::di[]

    # tag::create[]
    @View("bookscreate")  # <2>
    @Get("/create")  # <3>
    def create(self) -> dict:
        return self.create_model_with_blank_values()
    # end::create[]

    # tag::stock[]
    @Produces(MediaType.TEXT_PLAIN)
    @Get("/stock/{isbn}")
    def stock(self, isbn: str) -> int:
        raise OutOfStockException()
    # end::stock[]

    # tag::save[]
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)  # <4>
    @Post("/save")  # <5>
    def save(self, cmd: Annotated[CommandBookSave, Body, Valid]) -> HttpResponse:  # <6>
        return HttpResponse.ok()
    # end::save[]

    # tag::onSavedFailed[]
    @View("bookscreate")
    @Error(exception=ConstraintViolationException)  # <2>
    @Status(HttpStatus.BAD_REQUEST)
    def on_saved_failed(
        self,
        cmd: Annotated[CommandBookSave, Body],
        ex: ConstraintViolationException,
    ) -> dict:  # <3>
        return {
            "title": cmd.title,
            "pages": cmd.pages,
            "errors": self.message_source.violations_messages(ex.get_constraint_violations()),
        }
    # end::onSavedFailed[]

    # tag::createModelWithBlankValues[]
    def create_model_with_blank_values(self) -> dict:
        return {"title": "", "pages": ""}
    # end::createModelWithBlankValues[]
    # tag::endOfFile[]
    # end::endOfFile[]
