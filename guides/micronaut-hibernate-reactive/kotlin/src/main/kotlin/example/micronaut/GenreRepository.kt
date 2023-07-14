package example.micronaut

import example.micronaut.domain.Genre
import org.reactivestreams.Publisher
import jakarta.validation.constraints.NotBlank

interface GenreRepository {
    fun findById(id: Long): Publisher<Genre?>
    fun save(name: String): Publisher<Genre>
    fun saveWithException(@NotBlank name: String): Publisher<Genre?>
    fun deleteById(id: Long)
    fun findAll(args: SortingAndOrderArguments): Publisher<Genre?>
    fun update(id: Long, @NotBlank name: String?): Publisher<Int?>
}