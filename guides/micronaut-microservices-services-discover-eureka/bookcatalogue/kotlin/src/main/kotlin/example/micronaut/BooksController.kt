package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/books") // <1>
class BooksController {

    @Get // <2>
    fun index(): List<Book> = listOf(
            Book("1491950358", "Building Microservices"),
            Book("1680502395", "Release It!"),
            Book("0321601912", "Continuous Delivery:"))
}
