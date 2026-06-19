package example.micronaut.dto;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.beans.BeanIntrospection;
import io.micronaut.core.beans.BeanProperty;
import io.micronaut.core.util.StringUtils;
import io.micronaut.json.JsonMapper;
import io.micronaut.serde.ObjectMapper;
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
class EmployeeDtoTest {

    @Test
    void employeeDtoIsIntrospected() {
        BeanIntrospection<EmployeeDto> introspection = assertDoesNotThrow(() -> BeanIntrospection.getIntrospection(EmployeeDto.class));

        Set<String> propertyNames = introspection.getBeanProperties()
                .stream()
                .map(BeanProperty::getName)
                .collect(Collectors.toSet());

        assertEquals(Set.of("id", "name", "salary", "phone"), propertyNames);
        assertTrue(introspection.getProperty("id", Long.class).isPresent());
        assertTrue(introspection.getProperty("name", String.class).isPresent());
        assertTrue(introspection.getProperty("salary", BigDecimal.class).isPresent());
        assertTrue(introspection.getProperty("phone", String.class).isPresent());
    }

    @Test
    void employeeDtoCanBeSerializedAndDeserialized(JsonMapper jsonMapper) throws IOException {
        EmployeeDto dto = new EmployeeDto(
                42L,
                "Sherlock Holmes",
                new BigDecimal("12345.67"),
                "+1-555-0100");

        String json = jsonMapper.writeValueAsString(dto);
        EmployeeDto decoded = jsonMapper.readValue(json, EmployeeDto.class);

        assertEquals("""
        {"id":42,"name":"Sherlock Holmes","salary":12345.67,"phone":"+1-555-0100"}""", json);
        assertEquals(dto, decoded);
    }
}
