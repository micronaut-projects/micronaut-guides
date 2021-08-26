package example.micronaut

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/books") // <1>
class BooksController {

    @Get // <2>
    fun index(): List<Book> {
        val buildingMicroservices = Book("1491950358", "Building Microservices")
        val releaseIt = Book("1680502395", "Release It!")
        val cidelivery = Book("0321601912", "Continuous Delivery:")
        return listOf(buildingMicroservices, releaseIt, cidelivery)
    }
}
