package example.micronaut;

import example.micronaut.domain.Genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    Optional<Genre> findById(long id);

    Genre save(@NotBlank String name);

    Genre saveWithException(@NotBlank String name);

    void deleteById(long id);

    List<Genre> findAll(@NotNull SortingAndOrderArguments args);

    int update(long id, @NotBlank String name);
}
