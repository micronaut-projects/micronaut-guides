/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;

import java.util.Optional;

@JdbcRepository(dialect = Dialect.H2) // <1>
public interface ContactRepository extends CrudRepository<ContactEntity, Long> { // <2>
    @Join(value = "phones", type = Join.Type.LEFT_FETCH) // <3>
    Optional<ContactEntity> getById(@NonNull Long id);

    @Query("select id, first_name, last_name from contact where id = :id") // <4>
    Optional<ContactPreview> findPreviewById(@NonNull Long id);

    @Query("""
select c.id, c.first_name, c.last_name, group_concat(p.phone) as phones
 from contact c
 left outer join phone p on c.id = p.contact_id
 where c.id = :id
 group by c.id""") // <5>
    Optional<ContactComplete> findCompleteById(@NonNull Long id);
}
