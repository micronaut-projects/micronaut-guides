package example.micronaut

import javax.transaction.Transactional
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

import example.micronaut.domain.Genre
import io.micronaut.core.annotation.NonNull
import io.micronaut.data.annotation.Id
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.reactive.ReactorPageableRepository
import reactor.core.publisher.Mono

@R2dbcRepository(dialect = Dialect.MYSQL) // <1>
interface GenreRepository extends ReactorPageableRepository<Genre, Long> { // <2>

    Mono<Genre> save(@NonNull @NotBlank String name);

    @Transactional
    default Mono<Genre> saveWithException(@NonNull @NotBlank String name) {
        return save(name)
                .then(Mono.error(new DataAccessException("test exception")))
    }

    Mono<Long> update(@NonNull @NotNull @Id Long id, @NonNull @NotBlank String name);
}
