package example.micronaut

import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.instrumentation.annotations.SpanAttribute
import io.opentelemetry.instrumentation.annotations.WithSpan
import jakarta.inject.Singleton
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.abs

@Singleton
open class InventoryService(private val tracer: Tracer, // <1>
                            private val warehouse: WarehouseClient) {

    private val inventory : ConcurrentHashMap<String, Int> = ConcurrentHashMap()

    private val storeName : String = "my_store"

    init {
        inventory["laptop"]  = 4
        inventory["desktop"] = 2
        inventory["monitor"] = 11
    }

    fun getProductNames(): Collection<String> = inventory.keys

    @WithSpan("stock-counts") // <2>
    open fun getStockCounts(@SpanAttribute("inventory.item") item : String): Map<String, Int> { //<3>
        val counts = mutableMapOf<String, Int>()
        if(inventory.containsKey(item)) {
            val count = inventory[item]!!
            counts["store"] = count

            if(count < 10) {
                counts["warehouse"] = inWarehouse(storeName, item)
            }
        }

        return counts
    }

    private fun inWarehouse(store: String, item: String): Int {
        Span.current().setAttribute("inventory.store-name", store) // <4>

        return warehouse.getItemCount(store, getUPC(item))
    }

    fun order(item : String, count: Int) {
        orderFromWarehouse(item, count)

        inventory[item] = count + (inventory[item] ?:  0)
    }

    private fun orderFromWarehouse(item: String, count: Int) {
        val span = tracer.spanBuilder("warehouse-order") // <5>
            .setAttribute("item", item)
            .setAttribute("count", count.toLong())
            .startSpan()

        val json = mapOf("store"   to storeName,
            "product" to item,
            "amount" to count,
            "upc" to getUPC(item))

        warehouse.order(json)

        span.end()
    }

    private fun getUPC(item: String): Int = abs(item.hashCode())

}


