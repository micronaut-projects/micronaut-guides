package example.micronaut;

import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.*;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.tracing.annotation.ContinueSpan;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExecuteOn(TaskExecutors.IO)
@Controller("/store")
public class StoreController {
    private final InventoryService inventory;

    public StoreController(InventoryService inventory) {
        this.inventory = inventory;
    }

    @Get("/inventory")
    @NewSpan("inventory-all") // <1>
    public List<Map<String, Object>> getInventory() {
        List<Map<String, Object>> currentInventory = new ArrayList<>();
        for (String product: inventory.getProductNames()) {
            currentInventory.add(getInventory(product));
        }
        return currentInventory;
    }

    @Post("/order")
    @Status(HttpStatus.CREATED)
    @ContinueSpan // <2>
    public void order(@SpanTag("order.item") String item, @SpanTag int count) { // <3> <4>
        inventory.order(item, count);
    }

    @Get("/inventory/{item}")
    public Map<String, Object> getInventory(String item) { // <5>
        Map<String, Object> counts = new HashMap<>(inventory.getStockCounts(item));
        if(counts.isEmpty()) {
            counts.put("note", "Not available at store");
        }

        counts.put("item", item);

        return counts;
    }
}
