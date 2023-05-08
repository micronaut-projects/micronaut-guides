package example.micronaut

import example.micronaut.genre.GenreRepository
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import jakarta.validation.ConstraintViolationException

@MicronautTest
class GenreRepositoryImplSpec extends Specification {

    @Inject
    GenreRepository genreRepository

    void constraintsAreValidatedForFindAll() {
        when:
        genreRepository.findAll(null)

        then:
        thrown(ConstraintViolationException)
    }

    void constraintsAreValidatedForSave() {
        when:
        genreRepository.save(null)

        then:
        thrown(ConstraintViolationException)
    }

    void constraintsAreValidatedForUpdateNameIsNull() {
        when:
        genreRepository.update(12, null)

        then:
        thrown(ConstraintViolationException)
    }

    void constraintsAreValidatedForUpdateNameIsBlank() {
        when:
        genreRepository.update(4, '')

        then:
        thrown(ConstraintViolationException)
    }
}
