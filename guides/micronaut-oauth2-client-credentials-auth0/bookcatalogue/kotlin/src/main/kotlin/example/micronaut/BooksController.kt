package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.rules.SecurityRule.IS_AUTHENTICATED
import io.micronaut.security.annotation.Secured

@Controller("/books")
class BooksController {

    @Get
    @Secured(IS_AUTHENTICATED) // <1>
    fun index(): List<Book> = listOf(
            Book("1491950358", "Building Microservices"),
            Book("1680502395", "Release It!"),
            Book("0321601912", "Continuous Delivery:"))
}
