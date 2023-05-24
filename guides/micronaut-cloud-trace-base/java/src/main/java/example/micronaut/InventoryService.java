package example.micronaut;

import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import jakarta.inject.Singleton;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class InventoryService {

    private final Tracer tracer;
    private final WarehouseClient warehouse;
    private final Map<String, Integer> inventory = new ConcurrentHashMap<>();

    private static final String storeName = "my_store";

    public InventoryService(Tracer tracer, WarehouseClient warehouse) { // <1>
        this.tracer    = tracer;
        this.warehouse = warehouse;

        inventory.put("laptop",  4);
        inventory.put("desktop", 2);
        inventory.put("monitor", 11);
    }

    public Collection<String> getProductNames() {
        return inventory.keySet();
    }

    @WithSpan("stock-counts") // <2>
    public Map<String, Integer> getStockCounts(@SpanAttribute("inventory.item") String item) { // <3>
        HashMap<String, Integer> counts = new HashMap<>();
        if(inventory.containsKey(item)) {
            int count = inventory.get(item);
            counts.put("store", count);

            if(count < 10) {
                counts.put("warehouse", inWarehouse(storeName, item));
            }
        }

        return counts;
    }

    private int inWarehouse(String store, String item) {
        Span.current().setAttribute("inventory.store-name", store); // <4>

        return warehouse.getItemCount(store, getUPC(item));
    }

    public void order(String item, int count) {
        orderFromWarehouse(item, count);
        if(inventory.containsKey(item)) {
            count += inventory.get(item);
        }
        inventory.put(item, count);
    }

    private void orderFromWarehouse(String item, int count) {
        Span span = tracer.spanBuilder("warehouse-order") // <5>
                          .setAttribute("item", item)
                          .setAttribute("count", count)
                          .startSpan();

        Map<String, Object> json = new HashMap<>(4);
        json.put("store", storeName);
        json.put("product", item);
        json.put("amount", count);
        json.put("upc", getUPC(item));

        warehouse.order(json);

        span.end();
    }

    private int getUPC(String item) {
        return Math.abs(item.hashCode());
    }
}
