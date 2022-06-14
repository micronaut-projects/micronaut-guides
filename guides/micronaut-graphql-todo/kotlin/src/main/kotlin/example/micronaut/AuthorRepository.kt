package example.micronaut

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect.POSTGRES
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = POSTGRES) // <1>
abstract class AuthorRepository : CrudRepository<Author, Long> { // <2>

    abstract fun findByUsername(username: String): Author? // <3>

    abstract fun findByIdIn(ids: Collection<Long>): Collection<Author> // <4>

    fun findOrCreate(username: String): Author { // <5>
        return findByUsername(username) ?: save(Author(username))
    }
}