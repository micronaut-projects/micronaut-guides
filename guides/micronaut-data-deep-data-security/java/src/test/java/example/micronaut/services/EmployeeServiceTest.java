package example.micronaut.services;

import example.micronaut.Oracle;
import example.micronaut.dto.SalaryAggregates;
import example.micronaut.dto.SalarySummaryDto;
import example.micronaut.repository.EmployeeRepository;
import io.micronaut.core.annotation.NonNull;
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

@Sql({"classpath:employees-test-data.sql"})
@MicronautTest(startApplication = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeServiceTest implements TestPropertyProvider {
    @Override
    public @NonNull Map<String, String> getProperties() {
        return Oracle.getProperties();
    }

    @Test
    void employeeModel(EmployeeService service) {
        Map<String, Object> model = service.employeeModel(Authentication.build("emma@supremo.onmicrosoft.com"));
        assertInstanceOf(Collection.class, model.get("employees"));
        assertEquals(5, ((Collection) model.get("employees")).size());
        assertEquals(new SalarySummaryDto(new BigDecimal("6900"), new BigDecimal("13000"), new BigDecimal("9826.00"), 5L),
                model.get("salarySummary"));
    }
}
