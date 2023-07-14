package example.micronaut.genre;

import example.micronaut.ListingArguments;
import example.micronaut.domain.Genre;
import io.micronaut.core.annotation.NonNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    @NonNull
    Optional<Genre> findById(long id);

    @NonNull
    Genre save(@NonNull @NotBlank String name);

    void deleteById(long id);

    @NonNull
    List<Genre> findAll(@NonNull @NotNull ListingArguments args);

    int update(long id, @NonNull @NotBlank String name);
}
