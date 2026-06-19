package example.micronaut.repository;

import example.micronaut.Oracle;
import example.micronaut.dto.SalaryAggregates;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.test.annotation.Sql;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Sql({"classpath:employees-test-data.sql"})
@MicronautTest(startApplication = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeRepositoryTest implements TestPropertyProvider {
    @Override
    public @NonNull Map<String, String> getProperties() {
        return Oracle.getProperties();
    }

    @Test
    void findAll(EmployeeRepository repository) {
        assertEquals(5, repository.findAll().size());
    }

    @Test
    void getSalaryAggregates(EmployeeRepository repository) {
        assertEquals(new SalaryAggregates(new BigDecimal("6900"), new BigDecimal("13000"), new BigDecimal("49130"), 5L), repository.getSalaryAggregates());
    }
}
