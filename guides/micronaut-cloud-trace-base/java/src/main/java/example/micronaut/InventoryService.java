package example.micronaut;

import io.micronaut.tracing.annotation.ContinueSpan;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import io.opentracing.Span;
import io.opentracing.Tracer;
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

    @NewSpan
    public Map<String, Integer> getStockCounts(@SpanTag("inventory.item") String item) {
        HashMap<String, Integer> counts = new HashMap<>();
        if(inventory.containsKey(item)) {
            int count = inStore(item);
            counts.put("store", count);

            if(count < 10) {
                counts.put("warehouse", inWarehouse(storeName, item));
            }
        }

        return counts;
    }

    private int inStore(String item) {
        int count = inventory.get(item);
        tracer.activeSpan().setTag("inventory.store.count", count); // <2>
        return count;
    }

    @ContinueSpan // <3>
    protected int inWarehouse(@SpanTag String store, String item) {
        int count = warehouse.getItemCount(store, getUPC(item));

        tracer.activeSpan().setTag("inventory.warehouse.count", count); // <4>

        return count;
    }

    public void order(String item, int count) {
        orderFromWarehouse(item, count);
        if(inventory.containsKey(item)) {
            count += inventory.get(item);
        }
        inventory.put(item, count);
    }

    private void orderFromWarehouse(String item, int count) {
        Span span = tracer.buildSpan("warehouse-order") // <5>
                .withTag("item", item)
                .withTag("count", count)
                .start();

        Map<String, Object> json = new HashMap<>(4);
        json.put("store", storeName);
        json.put("product", item);
        json.put("amount", count);
        json.put("upc", getUPC(item));

        warehouse.order(json);

        span.finish();
    }

    private int getUPC(String item) {
        return Math.abs(item.hashCode());
    }
}
