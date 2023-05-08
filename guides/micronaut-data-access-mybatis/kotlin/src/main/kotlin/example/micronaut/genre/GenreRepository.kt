package example.micronaut.genre

import example.micronaut.ListingArguments
import example.micronaut.domain.Genre
import java.util.Optional
import jakarta.validation.constraints.NotBlank

interface GenreRepository {

    fun findById(id: Long): Optional<Genre>

    fun save(@NotBlank name: String): Genre

    fun deleteById(id: Long)

    fun findAll(args: ListingArguments): List<Genre>

    fun update(id: Long, @NotBlank name: String): Int
}
