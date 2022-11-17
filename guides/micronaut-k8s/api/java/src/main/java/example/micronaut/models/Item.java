package example.micronaut.models;

import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Introspected
public class Item {

    public Item(Integer id, String name,  BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    private Integer id;

    private BigDecimal price;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
