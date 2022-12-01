package example.micronaut.models;

import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Serdeable // <1>
public record Item(
        Integer id,
        String name,
        BigDecimal price
) {
}
