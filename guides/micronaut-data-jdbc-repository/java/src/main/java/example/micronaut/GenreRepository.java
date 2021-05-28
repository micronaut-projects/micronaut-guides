package example.micronaut;

import example.micronaut.domain.Genre;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
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

    Optional<Genre> findById(@NotNull Long id);

    Genre save(@NotBlank String name);

    @Transactional
    default Genre saveWithException(@NotBlank String name) {
        save(name);
        throw new DataAccessException("test exception");
    }

    void deleteById(@NotNull Long id);

    List<Genre> findAll();

    List<Genre> findAllOrderById();

    List<Genre> findAllOrderByIdDesc();

    List<Genre> findAllOrderByName();

    List<Genre> findAllOrderByNameDesc();

    default List<Genre> findAll(@NotNull SortingAndOrderArguments args) {
        List<Genre> genres = null;
        if (args.getSort().isPresent()) {
            if (args.getSort().get().equals("name")) {
                genres = args.getSort().isPresent() && args.getSort().get().equals("desc")
                        ? findAllOrderByNameDesc() : findAllOrderByName();
            } else if (args.getSort().get().equals("id")) {
                genres = args.getSort().isPresent() && args.getSort().get().equals("desc")
                        ? findAllOrderByIdDesc() : findAllOrderById();
            }
        }
        if (genres == null) {
            genres = findAll();
        }
        int offset = args.getOffset().orElse(0);
        if (offset >= genres.size()) {
            return Collections.emptyList();
        }
        if (args.getMax().isPresent()) {
            genres = genres.subList(offset, Math.min(genres.size(), args.getMax().get()));
        }
        return genres;
    }

    int update(@NotNull @Id Long id, @NotBlank String name);
}
