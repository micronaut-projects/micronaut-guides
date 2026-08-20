package example.micronaut

import example.micronaut.domain.Account
import io.micronaut.data.annotation.Id
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository

@JdbcRepository(dialect = Dialect.ORACLE)
interface AccountRepository : CrudRepository<Account, Long> {
    fun reserveIncrementBalanceAndDecrementCredit(@Id id: Long, balance: Long, credit: Long): Long
}
