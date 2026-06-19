package example.micronaut.services;

import example.micronaut.dto.SalaryAggregates;
import example.micronaut.dto.SalarySummaryDto;
import example.micronaut.repository.EmployeeRepository;
import io.micronaut.security.authentication.Authentication;
import jakarta.inject.Singleton;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

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

    public @NonNull Map<String, Object> employeeModel(@Nullable Authentication authentication) {
        Map<String, Object> model = new HashMap<>();
        if (authentication != null) {
            model.put("employees", employeeRepository.findAll());
            model.put("salarySummary", salarySummary(employeeRepository.getSalaryAggregates()));
        }
        return model;
    }

    private static SalarySummaryDto salarySummary(SalaryAggregates aggregates) {
        Long count = aggregates.count();
        long employeeCount = count == null ? 0 : count;
        BigDecimal averageSalary = null;
        if (aggregates.sum() != null && employeeCount > 0) {
            averageSalary = aggregates.sum().divide(BigDecimal.valueOf(employeeCount), 2, RoundingMode.HALF_UP);
        }
        return new SalarySummaryDto(aggregates.min(), aggregates.max(), averageSalary, employeeCount);
    }
}
