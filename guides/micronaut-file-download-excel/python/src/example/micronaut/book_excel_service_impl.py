import logging

from builders.dsl.spreadsheet.builder.poi import PoiSpreadsheetBuilder
from java.io import File, IOException
from jakarta.inject import Singleton
from micronaut.http import HttpStatus
from micronaut.http.exceptions import HttpStatusException
from micronaut.http.server.types.files import SystemFile

from .book import Book
from .book_excel_service import BookExcelService
from .book_excel_stylesheet import BookExcelStylesheet

LOG = logging.getLogger(__name__)


@Singleton  # <1>
class BookExcelServiceImpl(BookExcelService):
    def excel_file_from_books(self, book_list: list[Book]) -> SystemFile:
        try:
            file = File.createTempFile(
                self.HEADER_EXCEL_FILE_PREFIX,
                self.HEADER_EXCEL_FILE_SUFFIX,
            )
            PoiSpreadsheetBuilder.create(file).build(
                lambda workbook: self._build_workbook(workbook, book_list)
            )
            return SystemFile(file).attach(self.HEADER_EXCEL_FILENAME)
        except IOException:
            LOG.exception("File exception raised when generating excel file")

        raise HttpStatusException(
            HttpStatus.SERVICE_UNAVAILABLE,
            "error generating excel file",
        )

    def _build_workbook(self, workbook, book_list: list[Book]) -> None:
        workbook.apply(BookExcelStylesheet())
        workbook.sheet(
            self.SHEET_NAME,
            lambda sheet: self._build_sheet(sheet, book_list),
        )

    def _build_sheet(self, sheet, book_list: list[Book]) -> None:
        sheet.row(
            lambda row: [
                row.cell(
                    lambda cell, header=header: cell.value(header).style(
                        BookExcelStylesheet.STYLE_HEADER
                    )
                )
                for header in [self.HEADER_ISBN, self.HEADER_NAME]
            ]
        )
        for book in book_list:
            sheet.row(lambda row, book=book: [row.cell(book.isbn), row.cell(book.name)])
