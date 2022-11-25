package example.micronaut.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.serde.annotation.Serdeable
import java.math.BigDecimal
import javax.validation.constraints.Max
import javax.validation.constraints.NotBlank

@Serdeable
data class Order (
    @Max(10000) @JsonProperty val id:Int, // <1>
    @NotBlank @JsonProperty("user_id") val userId:Int,
    @JsonProperty val items: List<Item>?, // <2>
    @NotBlank @JsonProperty("item_ids") val itemIds:List<Int>?, // <3>
    val total: BigDecimal?)