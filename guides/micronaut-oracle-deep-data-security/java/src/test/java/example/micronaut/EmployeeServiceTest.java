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
import io.micronaut.security.authentication.Authentication;
import io.micronaut.test.annotation.Sql;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql("classpath:employees-test-data.sql")
@MicronautTest(startApplication = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@RunAs(name = "emma@supremo.onmicrosoft.com")
class EmployeeServiceTest implements TestPropertyProvider {

    @Override
    @NonNull
    public Map<String, String> getProperties() {
        return Oracle.getProperties();
    }

    @Test
    void employeeModel(EmployeeService service) {
        Map<String, Object> model = service.employeeModel(Authentication.build("emma@supremo.onmicrosoft.com"));
        Collection<?> employees = assertInstanceOf(Collection.class, model.get("employees"));
        assertEquals(5, employees.size());
        assertEquals(new SalarySummaryDto(new BigDecimal("6900"), new BigDecimal("13000"), new BigDecimal("9826.00"), 5L),
                model.get("salarySummary"));
    }

    @Test
    void employeeModelIsEmptyForAnonymousUsers(EmployeeService service) {
        assertTrue(service.employeeModel(null).isEmpty());
    }
}
