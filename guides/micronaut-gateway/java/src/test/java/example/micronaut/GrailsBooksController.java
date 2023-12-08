package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.List;

@Requires(property = "framework", value = "grails")
@Controller("/grails/books")
class GrailsBooksController {

    @Get
    List<Book> books() {
        return List.of(
                new Book("Grails 3 - Step by Step"),
                new Book("Grails Goodness Notebook"),
                new Book("Falando de Grails"),
                new Book("Grails in Action"),
                new Book("The Definitive Guide to Grails 2"),
                new Book("Proramming Grails")
        );
    }
}
