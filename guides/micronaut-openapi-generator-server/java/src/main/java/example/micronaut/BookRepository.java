package example.micronaut;

import example.micronaut.model.BookAvailability;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.GenericRepository;
import io.micronaut.data.repository.jpa.JpaSpecificationExecutor;
import io.micronaut.data.repository.jpa.criteria.PredicateSpecification;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@JdbcRepository(dialect = Dialect.MYSQL) // <1>
public interface BookRepository extends GenericRepository<BookEntity, Long>, // <2>
        JpaSpecificationExecutor<BookEntity> { // <3>

    @NonNull
    List<BookEntity> findAll(PredicateSpecification<BookEntity> spec);  // <4>

    @NonNull
    List<BookEntity> findAll();


    @NonNull
    BookEntity save(@NonNull @NotBlank String name,
                    @NonNull @NotNull BookAvailability availability,
                    @NonNull @NotBlank String author,
                    @NonNull @NotBlank String isbn);
}
