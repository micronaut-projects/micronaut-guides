package example.micronaut

import example.micronaut.genre.GenreRepository
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import jakarta.validation.ConstraintViolationException

@MicronautTest
class GenreRepositoryImplTest {

    @Inject
    lateinit var genreRepository: GenreRepository

    @Test
    fun constraintsAreValidatedForUpdateNameIsBlank() {
        assertThrows(ConstraintViolationException::class.java) { genreRepository.update(4, "") }
    }
}
