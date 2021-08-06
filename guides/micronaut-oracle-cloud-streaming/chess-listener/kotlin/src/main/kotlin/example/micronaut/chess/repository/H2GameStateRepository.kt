package example.micronaut.chess.repository

import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment.DEVELOPMENT
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect.H2

@Primary
@JdbcRepository(dialect = H2)
@Requires(env = [DEVELOPMENT])
interface H2GameStateRepository : GameStateRepository
