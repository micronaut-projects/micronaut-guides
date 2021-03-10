package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.types.files.SystemFile;
import io.micronaut.views.View;

import java.util.HashMap;
import java.util.Map;

@Controller // <1>
public class HomeController {

    protected final BookRepository bookRepository;
    protected final BookExcelService bookExcelService;

    public HomeController(BookRepository bookRepository,  // <2>
                          BookExcelService bookExcelService) {
        this.bookRepository = bookRepository;
        this.bookExcelService = bookExcelService;
    }

    @View("index") // <3>
    @Get
    public Map<String, String> index() {
        return new HashMap<>();
    }

    @Produces(value = "application/vnd.ms-excel")
    @Get("/excel") // <4>
    public SystemFile excel() { // <5>
        return bookExcelService.excelFileFromBooks(bookRepository.findAll());
    }
}
