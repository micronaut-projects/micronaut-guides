package example.micronaut.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Introspected
public record Order(
        @Max(10000) @JsonProperty Integer id, // <1>
        @NotBlank @JsonProperty("user_id") Integer userId,
        @JsonProperty List<Item> items, // <2>
        @NotBlank @JsonProperty("item_ids") List<Integer> itemIds, // <3>
        BigDecimal total
) {
}