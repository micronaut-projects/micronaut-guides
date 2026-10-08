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

import groovy.transform.CompileStatic
import io.micronaut.core.annotation.NonNull
import io.micronaut.core.annotation.Nullable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

import java.math.RoundingMode

@CompileStatic
@Singleton // <1>
class EmployeeService {

    private final EmployeeRepository employeeRepository

    EmployeeService(EmployeeRepository employeeRepository) { // <2>
        this.employeeRepository = employeeRepository
    }

    @NonNull
    Map<String, Object> employeeModel(@Nullable Authentication authentication) {
        Map<String, Object> model = [:]
        if (authentication != null) { // <3>
            model.employees = employeeRepository.findAll() // <4>
            model.salarySummary = salarySummary(employeeRepository.salaryAggregates) // <5>
        }
        model
    }

    private static SalarySummaryDto salarySummary(SalaryAggregates aggregates) {
        long employeeCount = aggregates.count == null ? 0 : aggregates.count
        BigDecimal averageSalary = null
        if (aggregates.sum != null && employeeCount > 0) {
            averageSalary = aggregates.sum.divide(BigDecimal.valueOf(employeeCount), 2, RoundingMode.HALF_UP)
        }
        new SalarySummaryDto(aggregates.min, aggregates.max, averageSalary, employeeCount)
    }
}
