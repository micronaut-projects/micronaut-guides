package example.micronaut

import io.micronaut.rabbitmq.annotation.Queue
import io.micronaut.rabbitmq.annotation.RabbitListener

@RabbitListener // <1>
class BookCatalogueService {

    @Queue("catalogue") // <2>
    fun listBooks(): List<Book> {
        val buildingMicroservices = Book("1491950358", "Building Microservices")
        val releaseIt = Book("1680502395", "Release It!")
        val cidelivery = Book("0321601912", "Continuous Delivery:")

        return listOf(buildingMicroservices, releaseIt, cidelivery)
    }
}
