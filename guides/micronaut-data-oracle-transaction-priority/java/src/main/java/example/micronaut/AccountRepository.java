package example.micronaut;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@JdbcRepository(dialect = Dialect.ORACLE)
// <1>
public interface AccountRepository extends CrudRepository<Account, Long> {

    // <2>
    @Query(value = "SELECT * FROM priority_account WHERE id = :id FOR UPDATE", nativeQuery = true)
    Optional<Account> findByIdForUpdate(Long id);
}
