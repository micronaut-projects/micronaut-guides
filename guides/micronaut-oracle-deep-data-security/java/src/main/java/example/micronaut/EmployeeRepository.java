/*
 * Copyright 2017-2026 original authors
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

import io.micronaut.data.annotation.Query;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.repository.CrudRepository;
import io.micronaut.security.annotation.RunAs;

@JdbcRepository(dialect = Dialect.ORACLE) // <1>
public interface EmployeeRepository extends CrudRepository<EmployeeEntity, Long> { // <2>

    @RunAs("ORACLE_DATA_ROLE_COMPENSATION_ANALYST") // <3>
    @Query("SELECT MIN(salary) AS min, MAX(salary) AS max, SUM(salary) AS sum, COUNT(*) AS count FROM hr.employees") // <4>
    SalaryAggregates getSalaryAggregates();
}
