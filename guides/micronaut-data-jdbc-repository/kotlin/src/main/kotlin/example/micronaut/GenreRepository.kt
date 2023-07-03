package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.data.annotation.Id
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository
import jakarta.transaction.Transactional
import jakarta.validation.constraints.NotBlank

@JdbcRepository(dialect = Dialect.MYSQL) //<1>
abstract class GenreRepository : PageableRepository<Genre, Long> { //<2>

    abstract fun save(@NotBlank name: String) : Genre

    @Transactional
    open fun saveWithException(@NotBlank name: String): Genre {
        save(name)
        throw DataAccessException("test exception")
    }

    abstract fun update(@Id id: Long, @NotBlank name: String) : Long
}