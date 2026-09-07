package example.micronaut;

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@JdbcRepository(dialect = Dialect.ORACLE) // <1>
public interface InventoryItemRepository extends CrudRepository<InventoryItem, Long> {

    @Query(value = "SELECT * FROM inventory_item WHERE id = :id FOR UPDATE", nativeQuery = true) // <2>
    Optional<InventoryItem> findByIdForUpdate(Long id);
}
