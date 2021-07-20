package example.micronaut.chess.repository;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.data.jdbc.annotation.JdbcRepository;

import static io.micronaut.context.env.Environment.ORACLE_CLOUD;
import static io.micronaut.data.model.query.builder.sql.Dialect.ORACLE;

/**
 * Oracle <code>GameState</code> entity repository.
 */
@Primary
@JdbcRepository(dialect = ORACLE)
@Requires(env = ORACLE_CLOUD)
public interface OracleGameStateRepository extends GameStateRepository {
}
