from typing import Protocol

from micronaut.http.server.types.files import SystemFile

from .book import Book


class BookExcelService(Protocol):
    SHEET_NAME = "Books"
    HEADER_ISBN = "Isbn"
    HEADER_NAME = "Name"
    HEADER_EXCEL_FILE_SUFFIX = ".xlsx"
    HEADER_EXCEL_FILE_PREFIX = "books"
    HEADER_EXCEL_FILENAME = HEADER_EXCEL_FILE_PREFIX + HEADER_EXCEL_FILE_SUFFIX

    def excel_file_from_books(self, book_list: list[Book]) -> SystemFile:  # <1>
        ...
