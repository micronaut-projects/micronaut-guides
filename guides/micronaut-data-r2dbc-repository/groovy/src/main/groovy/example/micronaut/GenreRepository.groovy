package example.micronaut

import jakarta.transaction.Transactional
import jakarta.validation.constraints.NotBlank

import example.micronaut.domain.Genre
import io.micronaut.data.annotation.Id
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.reactive.ReactorPageableRepository
import reactor.core.publisher.Mono

@R2dbcRepository(dialect = Dialect.MYSQL) // <1>
interface GenreRepository extends ReactorPageableRepository<Genre, Long> { // <2>

    Mono<Genre> save(@NotBlank String name);

    @Transactional
    default Mono<Genre> saveWithException(@NotBlank String name) {
        return save(name)
                .then(Mono.error(new DataAccessException("test exception")))
    }

    Mono<Long> update(@Id long id, @NotBlank String name);
}
