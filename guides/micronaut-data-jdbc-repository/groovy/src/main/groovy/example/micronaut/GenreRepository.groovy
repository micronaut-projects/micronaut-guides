package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.core.annotation.NonNull
import io.micronaut.data.annotation.Id
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

import javax.transaction.Transactional
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@JdbcRepository(dialect = Dialect.MYSQL) // <1>
abstract class GenreRepository implements PageableRepository<Genre, Long> { // <2>

    abstract Genre save(@NonNull @NotBlank String name)

    @Transactional
    Genre saveWithException(@NonNull @NotBlank String name) {
        save(name)
        throw new DataAccessException('test exception')
    }

    abstract long update(@NonNull @NotNull @Id Long id, @NonNull @NotBlank String name)
}
