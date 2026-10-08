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

import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton
import java.math.BigDecimal
import java.math.RoundingMode

@Singleton // <1>
class EmployeeService(private val employeeRepository: EmployeeRepository) { // <2>

    fun employeeModel(authentication: Authentication?): Map<String, Any> {
        val model = mutableMapOf<String, Any>()
        if (authentication != null) { // <3>
            model["employees"] = employeeRepository.findAll() // <4>
            model["salarySummary"] = salarySummary(employeeRepository.getSalaryAggregates()) // <5>
        }
        return model
    }

    private fun salarySummary(aggregates: SalaryAggregates): SalarySummaryDto {
        val employeeCount = aggregates.count ?: 0L
        val sum = aggregates.sum
        val averageSalary = if (sum != null && employeeCount > 0) {
            sum.divide(BigDecimal.valueOf(employeeCount), 2, RoundingMode.HALF_UP)
        } else {
            null
        }
        return SalarySummaryDto(aggregates.min, aggregates.max, averageSalary, employeeCount)
    }
}
