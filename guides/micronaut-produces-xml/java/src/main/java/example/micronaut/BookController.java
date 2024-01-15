package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;

@Controller("/book") // <1>
class BookController {
    @Produces(MediaType.APPLICATION_XML) // <2>
    @Get // <3>
    Book index() {
        return new Book("Building Microservices", "1491950358");
    }
}
