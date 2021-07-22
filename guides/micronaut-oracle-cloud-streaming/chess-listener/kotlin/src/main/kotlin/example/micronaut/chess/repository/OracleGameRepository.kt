package example.micronaut.chess.repository

import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment.ORACLE_CLOUD
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect.ORACLE

/**
 * Oracle `Game` entity repository.
 */
@Primary
@JdbcRepository(dialect = ORACLE) // <1>
@Requires(env = [ORACLE_CLOUD]) // <2>
interface OracleGameRepository : GameRepository
