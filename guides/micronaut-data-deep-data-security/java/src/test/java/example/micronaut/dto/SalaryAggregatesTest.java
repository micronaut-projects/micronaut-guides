package example.micronaut.dto;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanProperty;
import io.micronaut.core.util.StringUtils;
import io.micronaut.json.JsonMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Property(name = "datasources.default.enabled", value = StringUtils.FALSE)
@MicronautTest(startApplication = false)
class SalaryAggregatesTest {

    @Test
    void salaryAggregatesIsIntrospected() {
        BeanIntrospection<SalaryAggregates> introspection = assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(SalaryAggregates.class));

        Set<String> propertyNames = introspection.getBeanProperties()
                .stream()
                .map(BeanProperty::getName)
                .collect(Collectors.toSet());

        assertEquals(Set.of("min", "max", "sum", "count"), propertyNames);
        assertTrue(introspection.getProperty("min", BigDecimal.class).isPresent());
        assertTrue(introspection.getProperty("max", BigDecimal.class).isPresent());
        assertTrue(introspection.getProperty("sum", BigDecimal.class).isPresent());
        assertTrue(introspection.getProperty("count", Long.class).isPresent());
    }

    @Test
    void salaryAggregatesCanBeSerializedAndDeserialized(JsonMapper jsonMapper) throws IOException {
        SalaryAggregates aggregates = new SalaryAggregates(
                new BigDecimal("1000.50"),
                new BigDecimal("9000.75"),
                new BigDecimal("25000.25"),
                7L);

        String json = jsonMapper.writeValueAsString(aggregates);
        SalaryAggregates decoded = jsonMapper.readValue(json, SalaryAggregates.class);

        assertEquals("""
        {"min":1000.50,"max":9000.75,"sum":25000.25,"count":7}""", json);
        assertEquals(aggregates, decoded);
    }
}
