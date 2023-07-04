package example.micronaut

import builders.dsl.spreadsheet.builder.api.CellDefinition
import builders.dsl.spreadsheet.builder.api.RowDefinition
import builders.dsl.spreadsheet.builder.api.SheetDefinition
import builders.dsl.spreadsheet.builder.api.WorkbookDefinition
import builders.dsl.spreadsheet.builder.poi.PoiSpreadsheetBuilder
import example.micronaut.BookExcelService.Companion.HEADER_EXCEL_FILE_PREFIX
import example.micronaut.BookExcelService.Companion.HEADER_EXCEL_FILE_SUFIX
import example.micronaut.BookExcelService.Companion.HEADER_ISBN
import example.micronaut.BookExcelService.Companion.HEADER_NAME
import example.micronaut.BookExcelService.Companion.SHEET_NAME
import example.micronaut.BookExcelServiceImpl
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.http.server.types.files.SystemFile
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.util.stream.Stream
import jakarta.validation.Valid

@Singleton // <1>
open class BookExcelServiceImpl : BookExcelService {

    override fun excelFileFromBooks(bookList: List<@Valid Book>): SystemFile {
        try {
            val file = File.createTempFile(HEADER_EXCEL_FILE_PREFIX, HEADER_EXCEL_FILE_SUFIX)
            PoiSpreadsheetBuilder.create(file).build { w: WorkbookDefinition ->
                w.apply(BookExcelStylesheet::class.java)
                w.sheet(SHEET_NAME) { s: SheetDefinition ->
                    s.row { r: RowDefinition ->
                        Stream.of(HEADER_ISBN, HEADER_NAME)
                                .forEach { header: String? ->
                                    r.cell { cd: CellDefinition ->
                                        cd.value(header)
                                        cd.style(BookExcelStylesheet.STYLE_HEADER)
                                    }
                                }
                    }
                    for ((isbn, name) in bookList) {
                        s.row { r: RowDefinition ->
                            r.cell(isbn)
                            r.cell(name)
                        }
                    }
                }
            }
            return SystemFile(file).attach(BookExcelService.HEADER_EXCEL_FILENAME)
        } catch (e: IOException) {
            LOG.error("File not found exception raised when generating excel file")
        }
        throw HttpStatusException(HttpStatus.SERVICE_UNAVAILABLE, "error generating excel file")
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(BookExcelServiceImpl::class.java)
    }
}
