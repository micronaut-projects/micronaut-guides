package example.micronaut.chess.repository;

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;
import io.micronaut.data.jdbc.annotation.JdbcRepository;

import static io.micronaut.context.env.Environment.DEVELOPMENT;
import static io.micronaut.data.model.query.builder.sql.Dialect.H2;

@Primary
@JdbcRepository(dialect = H2) // <1>
@Requires(env = DEVELOPMENT) // <2>
public interface H2GameRepository extends GameRepository {
}
