package example.micronaut.repository

import example.micronaut.domain.Thing
import io.micronaut.core.annotation.NonNull
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.ORACLE)
interface ThingRepository extends CrudRepository<Thing, Long> {

    @Override
    @NonNull
    List<Thing> findAll()

    Optional<Thing> findByName(String name)
}
