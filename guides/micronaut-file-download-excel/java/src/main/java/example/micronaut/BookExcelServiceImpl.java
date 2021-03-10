package example.micronaut;

import builders.dsl.spreadsheet.builder.poi.PoiSpreadsheetBuilder;
import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.http.server.types.files.SystemFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Singleton // <1>
public class BookExcelServiceImpl implements BookExcelService {

    private static final Logger LOG = LoggerFactory.getLogger(BookExcelServiceImpl.class);

    @NonNull
    public SystemFile excelFileFromBooks(@NonNull @NotNull List<@Valid Book> bookList) {
        try {
            File file = File.createTempFile(HEADER_EXCEL_FILE_PREFIX, HEADER_EXCEL_FILE_SUFIX);
            PoiSpreadsheetBuilder.create(file).build(w -> {
                w.apply(BookExcelStylesheet.class);
                w.sheet(SHEET_NAME, s -> {
                    s.row(r -> Stream.of(HEADER_ISBN, HEADER_NAME)
                            .forEach(header -> r.cell(cd -> {
                                    cd.value(header);
                                    cd.style(BookExcelStylesheet.STYLE_HEADER);
                                })
                            ));
                    bookList.stream()
                            .forEach( book -> s.row(r -> {
                                r.cell(book.getIsbn());
                                r.cell(book.getName());
                            }));
                });
            });
            return new SystemFile(file).attach(HEADER_EXCEL_FILENAME);
        } catch (IOException e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("File not found exception raised when generating excel file");
            }
        }
        throw new HttpStatusException(HttpStatus.SERVICE_UNAVAILABLE, "error generating excel file");
    }
}