package example.micronaut.repositories;

import example.micronaut.domain.NameDto;
import example.micronaut.domain.Pet;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@JdbcRepository(dialect = Dialect.MYSQL)
public interface PetRepository extends PageableRepository<Pet, UUID> {

    List<NameDto> list(Pageable pageable);

    @Join("owner")
    Optional<Pet> findByName(String name);
}