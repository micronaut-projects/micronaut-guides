package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@JdbcRepository(dialect = Dialect.H2) // <1>
public interface ContactRepository extends CrudRepository<ContactEntity, Long> { // <2>
    @Query("select id, first_name, last_name from contact where id = :id") // <3>
    Optional<ContactPreview> findPreviewById(@NonNull Long id);

    @Query("""
select c.id, c.first_name, c.last_name, group_concat(p.phone) as phones
 from contact c
 left outer join phone p on c.id = p.contact_id
 where c.id = :id
 group by c.id""") // <3>
    Optional<ContactComplete> findCompleteById(@NonNull Long id);
}
