package example.micronaut

import io.micronaut.context.annotation.DefaultImplementation

@DefaultImplementation(BookRepositoryImpl::class)
interface BookRepository {

    fun findAll(): List<Book>
}
