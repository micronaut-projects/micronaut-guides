package example.micronaut

import io.micronaut.core.annotation.NonNull
import io.micronaut.context.annotation.DefaultImplementation
import io.micronaut.http.server.types.files.SystemFile
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

@DefaultImplementation(BookExcelServiceImpl)
interface BookExcelService {
    String SHEET_NAME = "Books"
    String HEADER_ISBN = "Isbn"
    String HEADER_NAME = "Name"
    String HEADER_EXCEL_FILE_SUFIX = ".xlsx"
    String HEADER_EXCEL_FILE_PREFIX = "books"
    String HEADER_EXCEL_FILENAME = HEADER_EXCEL_FILE_PREFIX + HEADER_EXCEL_FILE_SUFIX;

    @NonNull
    SystemFile excelFileFromBooks(@NonNull @NotNull List<@Valid Book> bookList) // <1>
}
