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
package example.micronaut

import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.MappedProperty
import java.math.BigDecimal

@MappedEntity(value = "employees", schema = "hr") // <1>
data class EmployeeEntity(
    @field:Id @field:MappedProperty("employee_id") val employeeId: Long?, // <2>
    @field:MappedProperty("first_name") val firstName: String?,
    @field:MappedProperty("last_name") val lastName: String?,
    val email: String?,
    val manager: String?,
    val ssn: String?,
    val salary: BigDecimal?,
    val phone: String?
)
