package example.micronaut

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository
import example.micronaut.domain.StudentView
import java.util.Optional

@JdbcRepository(dialect = Dialect.ORACLE)
interface StudentViewRepository : PageableRepository<StudentView, Long> {
    fun findByName(name: String): Optional<StudentView>
}
