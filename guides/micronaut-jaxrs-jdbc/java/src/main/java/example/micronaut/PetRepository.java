package example.micronaut;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;
import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JdbcRepository(dialect = Dialect.MYSQL) // <1>
public interface PetRepository extends CrudRepository<Pet, Long> { // <2>

    @NonNull
    List<NameDto> list(); // <3>

    @NonNull
    Optional<Pet> findByName(@NonNull @NotBlank String name);

    Pet save(@NonNull @NotBlank String name,
             @NonNull @NotNull PetType type);
}