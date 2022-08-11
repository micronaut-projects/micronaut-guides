package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.Repository
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.repository.reactive.ReactorPageableRepository
import reactor.core.publisher.Mono
import javax.transaction.Transactional
import javax.validation.constraints.NotBlank

@Repository // <1>
abstract class GenreRepository: ReactorPageableRepository<Genre, Long> { // <2>

    fun save(@NotBlank name: String): Mono<Genre?> {
        return save(Genre(id = null, name = name))
    }

    @Transactional
    open fun saveWithException(@NotBlank name: String): Mono<Genre> {
        return save(name)
            .handle { _, sink ->
                sink.error(DataAccessException("test exception"))
            }
    }

    fun update(@Id id: Long, @NotBlank name: String): Mono<Genre> {
        return update(Genre(id = id, name = name))
    }
}