package example.micronaut.models

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Nullable
import io.micronaut.serde.annotation.Serdeable

@CompileStatic
@EqualsAndHashCode
@Serdeable // <1>
class Order {

    @Nullable Integer id // <2>

    @JsonProperty("user_id") Integer userId

    @Nullable List<Item> items // <3>

    @JsonProperty("item_ids") @Nullable List<Integer> itemIds // <4>

    @Nullable BigDecimal total

    @Creator
    Order(Integer id,
         @JsonProperty("user_id") Integer userId,
         @Nullable List<Item> items,
         @JsonProperty("item_ids") @Nullable List<Integer> itemIds,
         @Nullable BigDecimal total
    ) {
        this.id = id
        this.userId = userId
        this.items = items
        this.itemIds = itemIds
        this.total = total
    }

}
