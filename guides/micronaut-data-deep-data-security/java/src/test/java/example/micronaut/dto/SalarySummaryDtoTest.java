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
class SalarySummaryDtoTest {

    @Test
    void salarySummaryDtoIsIntrospected() {
        BeanIntrospection<SalarySummaryDto> introspection = assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(SalarySummaryDto.class));

        Set<String> propertyNames = introspection.getBeanProperties()
                .stream()
                .map(BeanProperty::getName)
                .collect(Collectors.toSet());

        assertEquals(Set.of("minSalary", "maxSalary", "averageSalary", "employeeCount"), propertyNames);
        assertTrue(introspection.getProperty("minSalary", BigDecimal.class).isPresent());
        assertTrue(introspection.getProperty("maxSalary", BigDecimal.class).isPresent());
        assertTrue(introspection.getProperty("averageSalary", BigDecimal.class).isPresent());
        assertTrue(introspection.getProperty("employeeCount", long.class).isPresent());
    }

    @Test
    void salarySummaryDtoCanBeSerializedAndDeserialized(JsonMapper jsonMapper) throws IOException {
        SalarySummaryDto summary = new SalarySummaryDto(
                new BigDecimal("1000.50"),
                new BigDecimal("9000.75"),
                new BigDecimal("3571.46"),
                7L);

        String json = jsonMapper.writeValueAsString(summary);
        SalarySummaryDto decoded = jsonMapper.readValue(json, SalarySummaryDto.class);

        assertEquals("""
        {"minSalary":1000.50,"maxSalary":9000.75,"averageSalary":3571.46,"employeeCount":7}""", json);
        assertEquals(summary, decoded);
    }
}
