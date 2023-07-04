package example.micronaut.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Serdeable // <1>
public record Order(
        @Nullable @Max(10000) Integer id,
        @NotBlank @Nullable @JsonProperty("user_id") Integer userId,
        @Nullable User user,
        @Nullable List<Item> items,
        @NotBlank @Nullable @JsonProperty("item_ids") List<Integer> itemIds,
        @Nullable BigDecimal total
) {
}