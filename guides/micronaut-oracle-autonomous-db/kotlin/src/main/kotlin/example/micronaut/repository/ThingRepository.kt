package example.micronaut.repository

import example.micronaut.domain.Thing
import io.micronaut.core.annotation.NonNull
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.Optional

@JdbcRepository(dialect = Dialect.ORACLE)
interface ThingRepository : CrudRepository<Thing, Long> {

    @NonNull
    override fun findAll(): List<Thing>

    fun findByName(name: String?): Optional<Thing>
}
