package example.micronaut.models;

import io.micronaut.serde.annotation.Serdeable;

import java.math.BigDecimal;
import java.util.List;

@Serdeable // <1>
public record Item(
        Integer id,
        String name,
        BigDecimal price
) {
    public static List<Item> items = List.of(
            new Item(1, "Banana", new BigDecimal("1.5")),
            new Item(2, "Kiwi", new BigDecimal("2.5")),
            new Item(3, "Grape", new BigDecimal("1.25"))
        );
}
