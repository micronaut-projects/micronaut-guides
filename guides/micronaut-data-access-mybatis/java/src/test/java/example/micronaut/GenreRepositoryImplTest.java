package example.micronaut;

import example.micronaut.genre.GenreRepository;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class GenreRepositoryImplTest {

    @Inject
    GenreRepository genreRepository;

    @Test
    public void constraintsAreValidatedForFindAll() {
        assertThrows(ConstraintViolationException.class, () -> {
            genreRepository.findAll(null);
        });
    }

    @Test
    public void constraintsAreValidatedForSave() {
        assertThrows(ConstraintViolationException.class, () -> {
            genreRepository.save(null);
        });
    }

    @Test
    public void constraintsAreValidatedForUpdateNameIsNull() {
        assertThrows(ConstraintViolationException.class, () -> {
            genreRepository.update(12, null);
        });
    }

    @Test
    public void constraintsAreValidatedForUpdateNameIsBlank() {
        assertThrows(ConstraintViolationException.class, () -> {
            genreRepository.update(4, "");
        });
    }
}
