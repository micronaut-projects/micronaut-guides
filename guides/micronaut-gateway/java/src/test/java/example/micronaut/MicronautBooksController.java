package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import java.util.List;

@Requires(property = "framework", value = "micronaut")
@Controller("/micronaut/books")
class MicronautBooksController {

    @Get
    List<Book> books() {
        return List.of(
                new Book("Introducing Micronaut: Build, Test, and Deploy Java Microservices on Oracle Cloud"),
                new Book("Building Microservices with Micronaut®")
        );
    }
}
