package example.micronaut;

import example.micronaut.domain.Genre;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@JdbcRepository(dialect = Dialect.H2) // <1>
public interface GenreRepository extends CrudRepository<Genre, Long> { // <2>

    Optional<Genre> findById(@NonNull @NotNull Long id);

    Genre save(@NonNull @NotBlank String name);

    @Transactional
    default Genre saveWithException(@NonNull @NotBlank String name) {
        save(name);
        throw new DataAccessException("test exception");
    }

    void deleteById(@NonNull @NotNull Long id);

    List<Genre> findAll();

    List<Genre> findAll(@NonNull @NotNull Pageable pageable);

    int update(@NonNull @NotNull @Id Long id, @NonNull @NotBlank String name);
}
