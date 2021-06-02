package example.micronaut;

import java.util.List;

import edu.umd.cs.findbugs.annotations.NonNull;
import io.micronaut.context.annotation.DefaultImplementation;
import io.micronaut.http.server.types.files.SystemFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@DefaultImplementation(BookExcelServiceImpl.class)
public interface BookExcelService {
    String SHEET_NAME = "Books";
    String HEADER_ISBN = "Isbn";
    String HEADER_NAME = "Name";
    String HEADER_EXCEL_FILE_SUFIX = ".xlsx";
    String HEADER_EXCEL_FILE_PREFIX = "books";
    String HEADER_EXCEL_FILENAME = HEADER_EXCEL_FILE_PREFIX + HEADER_EXCEL_FILE_SUFIX;

    @NonNull
    SystemFile excelFileFromBooks(@NonNull @NotNull List<@Valid Book> bookList); // <1>
}
