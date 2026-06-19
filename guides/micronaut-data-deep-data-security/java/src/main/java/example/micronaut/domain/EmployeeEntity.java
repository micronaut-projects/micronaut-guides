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
package example.micronaut.domain;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import org.jspecify.annotations.Nullable;

import java.math.BigDecimal;

@MappedEntity(value = "employees", schema = "hr") // <1>
public record EmployeeEntity(
        @Id @MappedProperty("employee_id") @Nullable Long employeeId, // <2>
        @MappedProperty("first_name") @Nullable String firstName, // <3>
        @MappedProperty("last_name") @Nullable String lastName, // <3>
        @Nullable String email,
        @Nullable String manager,
        @Nullable String ssn,
        @Nullable BigDecimal salary,
        @Nullable String phone) {
}
