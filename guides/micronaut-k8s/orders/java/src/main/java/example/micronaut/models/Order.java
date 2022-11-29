package example.micronaut.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Serdeable
public record Order(
        @Max(10000) @Nullable Integer id, // <1>
        @NotBlank @JsonProperty("user_id") Integer userId,
        @Nullable List<Item> items, // <2>
        @NotBlank @JsonProperty("item_ids") @Nullable List<Integer> itemIds, // <3>
        @Nullable BigDecimal total
) {
}