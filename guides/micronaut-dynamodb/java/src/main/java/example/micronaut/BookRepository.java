package example.micronaut;

import io.micronaut.core.annotation.NonNull;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    @NonNull
    List<Book> findAll();

    @NonNull
    Optional<Book> findById(@NonNull @NotBlank String id);

    void delete(@NonNull @NotBlank String id);

    @NonNull
    String save(@NonNull @NotBlank String isbn,
                @NonNull @NotBlank String name);
}
