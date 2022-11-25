package example.micronaut.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Serdeable
public record Order(
        @Max(10000) @JsonProperty Integer id,
        @NotBlank @Nullable @JsonProperty("user_id") Integer userId,
        @JsonProperty @Nullable User user,
        @JsonProperty @Nullable List<Item> items,
        @NotBlank @Nullable @JsonProperty("item_ids") List<Integer> itemIds,
        @Nullable BigDecimal total
) {
}