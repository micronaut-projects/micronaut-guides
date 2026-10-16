from io import BytesIO

from jakarta.inject import Singleton
from openpyxl import Workbook
from openpyxl.styles import Font

from .book import Book
from .book_excel_service import BookExcelService


@Singleton  # <1>
class BookExcelServiceImpl(BookExcelService):
    def excel_file_from_books(self, book_list: list[Book]) -> bytes:
        workbook = Workbook()
        sheet = workbook.active
        sheet.title = self.SHEET_NAME
        sheet.append([self.HEADER_ISBN, self.HEADER_NAME])
        for cell in sheet[1]:
            cell.font = Font(bold=True)
        for book in book_list:
            sheet.append([book.isbn, book.name])
        output = BytesIO()
        workbook.save(output)
        return output.getvalue()
