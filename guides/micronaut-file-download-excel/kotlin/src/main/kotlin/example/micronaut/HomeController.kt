package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.types.files.SystemFile
import io.micronaut.views.View

@Controller // <1>
class HomeController(private val bookRepository: BookRepository,  // <2>
                     private val bookExcelService: BookExcelService) {

    @View("index") // <3>
    @Get
    fun index(): Map<String, String> = HashMap()

    @Produces(value = ["application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"])
    @Get("/excel") // <4>
    fun excel(): SystemFile = // <5>
            bookExcelService.excelFileFromBooks(bookRepository.findAll())
}
