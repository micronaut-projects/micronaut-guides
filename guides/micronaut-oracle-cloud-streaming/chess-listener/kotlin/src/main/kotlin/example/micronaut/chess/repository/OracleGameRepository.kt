package example.micronaut.chess.repository

import io.micronaut.context.annotation.Primary
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment.ORACLE_CLOUD
import io.micronaut.context.env.Environment.TEST
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect.ORACLE

@Primary
@JdbcRepository(dialect = ORACLE) // <1>
@Requires(env = [ORACLE_CLOUD, TEST]) // <2>
interface OracleGameRepository : GameRepository
