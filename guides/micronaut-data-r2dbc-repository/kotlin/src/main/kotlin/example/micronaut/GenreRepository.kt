package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.data.annotation.Id
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.reactive.ReactorPageableRepository
import reactor.core.publisher.Mono
import jakarta.transaction.Transactional
import jakarta.validation.constraints.NotBlank

@R2dbcRepository(dialect = Dialect.MYSQL) // <1>
abstract class GenreRepository : ReactorPageableRepository<Genre, Long> { // <2>
    abstract fun save(@NotBlank name: String): Mono<Genre>

    @Transactional
    open fun saveWithException(@NotBlank name: String): Mono<Genre> {
        return save(name)
            .then(Mono.error(DataAccessException("test exception")))
    }

    abstract fun update(@Id id: Long, @NotBlank name: String): Mono<Long>
}