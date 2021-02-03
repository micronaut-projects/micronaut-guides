package example.micronaut.genre;

import edu.umd.cs.findbugs.annotations.NonNull;
import example.micronaut.ListingArguments;
import example.micronaut.domain.Genre;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    @NonNull
    Optional<Genre> findById(@NonNull @NotNull Long id);

    @NonNull
    Genre save(@NonNull @NotBlank String name);

    void deleteById(@NonNull @NotNull Long id);

    @NonNull
    List<Genre> findAll(@NonNull @NotNull ListingArguments args);

    int update(@NonNull @NotNull Long id, @NonNull @NotBlank String name);
}
