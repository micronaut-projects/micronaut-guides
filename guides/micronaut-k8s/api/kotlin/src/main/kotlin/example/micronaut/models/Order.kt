package example.micronaut.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable
import java.math.BigDecimal
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotBlank

@Serdeable // <1>
data class Order (
    @Nullable @Max(10000) val id:Int, // <2>
    @NotBlank @Nullable @JsonProperty("user_id") val userId:Int?,
    @Nullable val user: User?,
    val items: List<Item>?, // <3>
    @NotBlank @JsonProperty("item_ids") val itemIds:List<Int>?, // <4>
    val total: BigDecimal?)