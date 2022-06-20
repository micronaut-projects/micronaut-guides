package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.types.files.SystemFile
import io.micronaut.views.View

@Controller // <1>
class HomeController {

    private final BookRepository bookRepository
    private final BookExcelService bookExcelService

    HomeController(BookRepository bookRepository,  // <2>
                   BookExcelService bookExcelService) {
        this.bookRepository = bookRepository
        this.bookExcelService = bookExcelService
    }

    @View("index") // <3>
    @Get
    Map<String, String> index() {
        [:]
    }

    @Produces(value = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    @Get("/excel") // <4>
    SystemFile excel() { // <5>
        bookExcelService.excelFileFromBooks(bookRepository.findAll())
    }
}
