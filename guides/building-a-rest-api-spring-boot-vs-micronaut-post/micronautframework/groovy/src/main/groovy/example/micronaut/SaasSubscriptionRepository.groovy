package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@CompileStatic
@JdbcRepository(dialect = Dialect.H2) // <1>
interface SaasSubscriptionRepository extends CrudRepository<SaasSubscription, Long> { // <2>
}
