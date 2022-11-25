package example.micronaut.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Serdeable
public record Item(
        @JsonProperty("id") Integer id,
        @JsonProperty("name") String name,
        @JsonProperty("price") BigDecimal price
) {
    public static List<Item> items = Arrays.asList(
            new Item(1, "Banana", new BigDecimal("1.5")),
            new Item(2, "Kiwi", new BigDecimal("2.5")),
            new Item(3, "Grape", new BigDecimal("1.25"))
        );
}
