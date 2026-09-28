from micronaut.http.annotation import Controller, Get, Produces
from micronaut.http.server.types.files import SystemFile
from micronaut.views import View

from .book_excel_service import BookExcelService
from .book_repository import BookRepository


@Controller  # <1>
class HomeController:
    def __init__(
        self,
        book_repository: BookRepository,  # <2>
        book_excel_service: BookExcelService,
    ):
        self.book_repository = book_repository
        self.book_excel_service = book_excel_service

    @View("index")  # <3>
    @Get
    def index(self) -> dict:
        return {}

    @Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Get("/excel")  # <4>
    def excel(self) -> SystemFile:  # <5>
        return self.book_excel_service.excel_file_from_books(
            self.book_repository.find_all()
        )
