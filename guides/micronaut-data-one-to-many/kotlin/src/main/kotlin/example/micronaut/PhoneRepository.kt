package example.micronaut

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.H2) // <1>
interface PhoneRepository : CrudRepository<PhoneEntity, Long> { // <2>
    fun deleteByContact(contact: ContactEntity)
}
