package example.micronaut

import io.micronaut.context.annotation.DefaultImplementation
import io.micronaut.http.server.types.files.SystemFile

@DefaultImplementation(BookExcelServiceImpl::class)
interface BookExcelService {

    fun excelFileFromBooks(bookList: List<Book>): SystemFile // <1>

    companion object {
        const val SHEET_NAME = "Books"
        const val HEADER_ISBN = "Isbn"
        const val HEADER_NAME = "Name"
        const val HEADER_EXCEL_FILE_SUFIX = ".xlsx"
        const val HEADER_EXCEL_FILE_PREFIX = "books"
        const val HEADER_EXCEL_FILENAME = HEADER_EXCEL_FILE_PREFIX + HEADER_EXCEL_FILE_SUFIX
    }
}
