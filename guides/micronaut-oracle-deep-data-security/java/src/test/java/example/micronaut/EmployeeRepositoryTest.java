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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.annotation.RunAs;
import io.micronaut.test.annotation.Sql;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql("classpath:employees-test-data.sql") // <1>
@MicronautTest(startApplication = false) // <2>
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // <3>
@RunAs(name = "sergio") // <4>
class EmployeeRepositoryTest implements TestPropertyProvider { // <5>

    @Override
    @NonNull
    public Map<String, String> getProperties() {
        return Oracle.getProperties();
    }

    @Test
    void findAll(EmployeeRepository repository) {
        assertEquals(5, repository.findAll().size());
    }

    @Test
    void getSalaryAggregates(EmployeeRepository repository) {
        assertEquals(new SalaryAggregates(new BigDecimal("6900"), new BigDecimal("13000"), new BigDecimal("49130"), 5L),
                repository.getSalaryAggregates()); // <6>
    }
}
