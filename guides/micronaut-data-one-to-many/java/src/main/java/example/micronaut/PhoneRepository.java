package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.H2) // <1>
public interface PhoneRepository extends CrudRepository<PhoneEntity, Long> { // <2>
    void deleteByContact(@NonNull ContactEntity contact);
}
