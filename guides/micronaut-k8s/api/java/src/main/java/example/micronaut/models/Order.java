package example.micronaut.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Introspected
public record Order(
        @Max(10000) @JsonProperty Integer id,
        @NotBlank @JsonProperty("user_id") Integer userId,
        @JsonProperty User user,
        @JsonProperty List<Item> items,
        @NotBlank @JsonProperty("item_ids") List<Integer> itemIds,
        BigDecimal total
) {
}