package example.micronaut.chess.repository;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.data.jdbc.annotation.JdbcRepository;

import static io.micronaut.context.env.Environment.ORACLE_CLOUD;
import static io.micronaut.data.model.query.builder.sql.Dialect.ORACLE;

/**
 * Oracle <code>Game</code> entity repository.
 */
@Primary
@JdbcRepository(dialect = ORACLE) // <1>
@Requires(env = ORACLE_CLOUD) // <2>
public interface OracleGameRepository extends GameRepository {
}
