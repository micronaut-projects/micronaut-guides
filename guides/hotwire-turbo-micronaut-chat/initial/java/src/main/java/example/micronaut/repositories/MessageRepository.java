package example.micronaut.repositories;

import example.micronaut.entities.Message;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

@JdbcRepository(dialect = Dialect.MYSQL) // <1>
public interface MessageRepository extends CrudRepository<Message, Long> { // <2>
}
