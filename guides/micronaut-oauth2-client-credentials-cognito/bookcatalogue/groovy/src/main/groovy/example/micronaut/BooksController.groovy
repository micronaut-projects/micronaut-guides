package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import static io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED

@CompileStatic
@Controller("/books")
class BooksController {

    @Get
    @Secured(IS_AUTHENTICATED) // <1>
    List<Book> index() {
        [
            new Book("1491950358", "Building Microservices"),
            new Book("1680502395", "Release It!"),
            new Book("0321601912", "Continuous Delivery:")
        ]
    }
}
