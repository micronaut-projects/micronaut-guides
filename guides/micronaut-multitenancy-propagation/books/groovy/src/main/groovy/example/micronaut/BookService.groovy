package example.micronaut

import grails.gorm.multitenancy.CurrentTenant
import grails.gorm.services.Service

@CurrentTenant // <1>
@Service(Book) // <2>
interface BookService {
    Book save(String title)
    List<Book> findAll()
}
