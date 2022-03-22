package example.micronaut

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect.POSTGRES
import io.micronaut.data.repository.PageableRepository

@JdbcRepository(dialect = POSTGRES) // <1>
interface ToDoRepository : PageableRepository<ToDo?, Long?> // <2>
