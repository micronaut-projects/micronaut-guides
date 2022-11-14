package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.core.annotation.NonNull
import io.micronaut.data.annotation.Id
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.reactive.ReactorPageableRepository
import reactor.core.publisher.Mono
import javax.transaction.Transactional
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@R2dbcRepository(dialect = Dialect.MYSQL) // <1>
interface GenreRepository : ReactorPageableRepository<Genre, Long> { // <2>
    fun save(@NonNull name: @NotBlank String): Mono<Genre>

    @Transactional
    fun saveWithException(@NonNull name: @NotBlank String): Mono<Genre> {
        return save(name)
            .then(Mono.error(DataAccessException("test exception")))
    }

    fun update(@NonNull @Id id: @NotNull Long, @NonNull name: @NotBlank String): Mono<Long>
}