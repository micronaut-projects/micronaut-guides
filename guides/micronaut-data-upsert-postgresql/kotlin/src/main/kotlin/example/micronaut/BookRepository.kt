package example.micronaut

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.POSTGRES) // <1>
interface BookRepository : CrudRepository<Book, Long> { // <2>

    fun findByTitle(title: String): Book?

    fun upsert(book: Book) // <3>
}
