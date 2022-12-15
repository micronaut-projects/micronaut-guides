package example.micronaut.models;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;

@Serdeable // <1>
public record Item(
        Integer id,
        String name,
        BigDecimal price
) {
}
