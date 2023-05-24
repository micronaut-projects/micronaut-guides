package example.micronaut.models

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.serde.annotation.Serdeable
import java.math.BigDecimal

@Serdeable // <1>
data class Item (
    val id:Int,
    val name:String,
    val price: BigDecimal
) {
    companion object {
        var items: List<Item> = listOf(
            Item(1, "Banana", BigDecimal("1.5")),
            Item(2, "Kiwi", BigDecimal("2.5")),
            Item(3, "Grape", BigDecimal("1.25"))
        )
    }
}