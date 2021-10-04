package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.rabbitmq.annotation.Queue
import io.micronaut.rabbitmq.annotation.RabbitListener

@CompileStatic
@RabbitListener // <1>
class BookCatalogueService {

    @Queue('catalogue') // <2>
    List<Book> listBooks() {
        Book buildingMicroservices = new Book('1491950358', 'Building Microservices')
        Book releaseIt = new Book('1680502395', 'Release It!')
        Book cidelivery = new Book('0321601912', 'Continuous Delivery:')

        [buildingMicroservices, releaseIt, cidelivery]
    }
}
