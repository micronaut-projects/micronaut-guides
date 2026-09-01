package example.micronaut;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

@MappedEntity("inventory_item")
public record InventoryItem(
    @Id Long id,
    String name,
    int availableQuantity,
    Status status
) {
}
