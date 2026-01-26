package example.micronaut;

import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.PageableRepository;
import example.micronaut.domain.StudentView;

@JdbcRepository(dialect = Dialect.ORACLE)
public interface StudentViewRepository extends PageableRepository<StudentView, Long> {
}