package example.micronaut;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;
import io.micronaut.core.annotation.NonNull;
import javax.validation.constraints.NotBlank;

@JdbcRepository(dialect = Dialect.MYSQL) // <1>
public interface PetRepository extends CrudRepository<Pet, Long> { // <2>

    @NonNull
    List<NameDto> list(); // <3>

    @NonNull
    Optional<Pet> findByName(@NonNull @NotBlank String name);
}