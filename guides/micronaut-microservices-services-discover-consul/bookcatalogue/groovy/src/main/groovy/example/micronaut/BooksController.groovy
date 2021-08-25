package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@CompileStatic
@Controller("/books") // <1>
class BooksController {

    @Get // <2>
    List<Book> index() {
        Book buildingMicroservices = new Book("1491950358", "Building Microservices")
        Book releaseIt = new Book("1680502395", "Release It!")
        Book cidelivery = new Book("0321601912", "Continuous Delivery:")
        [buildingMicroservices, releaseIt, cidelivery]
    }
}
