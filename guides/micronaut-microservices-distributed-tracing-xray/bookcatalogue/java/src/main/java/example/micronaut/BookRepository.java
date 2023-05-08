package example.micronaut;

import io.micronaut.core.annotation.NonNull;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    boolean existsTable();

    void createTable();

    @NonNull
    List<Book> findAll();

    void save(@NonNull @NotNull @Valid Book book);

    @NonNull
    Optional<Book> findById(@NonNull @NotBlank String id);

    void delete(@NonNull @NotBlank String id);
}
