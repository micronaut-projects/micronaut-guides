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
import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Singleton // <1>
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) { // <2>
        this.employeeRepository = employeeRepository;
    }

    @NonNull
    public Map<String, Object> employeeModel(@Nullable Authentication authentication) {
        Map<String, Object> model = new HashMap<>();
        if (authentication != null) { // <3>
            model.put("employees", employeeRepository.findAll()); // <4>
            model.put("salarySummary", salarySummary(employeeRepository.getSalaryAggregates())); // <5>
        }
        return model;
    }

    private static SalarySummaryDto salarySummary(SalaryAggregates aggregates) {
        long employeeCount = aggregates.count() == null ? 0 : aggregates.count();
        BigDecimal averageSalary = null;
        if (aggregates.sum() != null && employeeCount > 0) {
            averageSalary = aggregates.sum().divide(BigDecimal.valueOf(employeeCount), 2, RoundingMode.HALF_UP);
        }
        return new SalarySummaryDto(aggregates.min(), aggregates.max(), averageSalary, employeeCount);
    }
}
