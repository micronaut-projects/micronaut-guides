package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED;
import java.util.Arrays;
import java.util.List;

@Controller("/books")
public class BooksController {

    @Get
    @Secured(IS_AUTHENTICATED) // <1>
    public List<Book> index() {
        return Arrays.asList(
                new Book("1491950358", "Building Microservices"),
                new Book("1680502395", "Release It!"),
                new Book("0321601912", "Continuous Delivery:")
        );
    }
}
