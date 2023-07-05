package example.micronaut.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable
import java.math.BigDecimal
import jakarta.validation.constraints.Max

@Serdeable // <1>
data class Order (
    @Nullable @Max(10000) val id:Int, // <2>
    @JsonProperty("user_id") val userId:Int,
    @Nullable val items: List<Item>?, // <3>
    @JsonProperty("item_ids") val itemIds:List<Int>?, // <4>
    @Nullable val total: BigDecimal?)