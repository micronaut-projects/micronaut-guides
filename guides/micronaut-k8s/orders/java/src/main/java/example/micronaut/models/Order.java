package example.micronaut.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.Max;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Introspected
public class Order {

    @Max(10000)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) // <1>
    private Integer id;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)  // <2>
    private List<Item> items = new ArrayList<>();

    @JsonProperty(value = "items_id", access = JsonProperty.Access.WRITE_ONLY) // <3>
    private List<Integer> itemIds = new ArrayList<>();
    private BigDecimal total;

    public List<Integer> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Integer> itemIds) {
        this.itemIds = itemIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}