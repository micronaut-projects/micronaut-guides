package example.micronaut;

import example.micronaut.domain.Genre;
import io.micronaut.core.annotation.NonNull;
import org.reactivestreams.Publisher;

import jakarta.validation.constraints.NotBlank;
import java.util.Optional;

public interface GenreRepository {

    Publisher<Optional<Genre>> findById(long id);

    Publisher<Genre> save(@NotBlank String name);

    Publisher<Genre> saveWithException(@NotBlank String name);

    void deleteById(long id);

    Publisher<Genre> findAll(@NonNull SortingAndOrderArguments args);

    Publisher<Integer> update(long id, @NotBlank String name);
}
