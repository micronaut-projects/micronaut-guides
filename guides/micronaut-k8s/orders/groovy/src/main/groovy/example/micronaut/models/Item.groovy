package example.micronaut.models

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import io.micronaut.core.annotation.Creator
import io.micronaut.serde.annotation.Serdeable


@CompileStatic
@EqualsAndHashCode
@Serdeable // <1>
class Item {

    Integer id
    String name
    BigDecimal price

    public static List<Item> items = [
            new Item(1, "Banana", new BigDecimal("1.5")),
            new Item(2, "Kiwi", new BigDecimal("2.5")),
            new Item(3, "Grape", new BigDecimal("1.25"))
    ]

    @Creator
    Item(Integer id,
         String name,
         BigDecimal price
    ) {
        this.id = id
        this.name = name
        this.price = price
    }
}
