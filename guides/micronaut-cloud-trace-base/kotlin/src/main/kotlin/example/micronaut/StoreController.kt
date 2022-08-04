package example.micronaut

import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.tracing.annotation.ContinueSpan
import io.micronaut.tracing.annotation.NewSpan
import io.micronaut.tracing.annotation.SpanTag

@ExecuteOn(TaskExecutors.IO)
@Controller("/store")
open class StoreController(private val inventory: InventoryService) {

    @Post("/order")
    @Status(HttpStatus.CREATED)
    @NewSpan("store.order") //<1>
    open fun order(@SpanTag("order.item") item: String, @SpanTag count: Int) = inventory.order(item, count) // <2>

    @Get("/inventory") // <3>
    fun getInventory(): List<Map<String, Any>> = inventory.getProductNames().map { getInventory(it) }

    @Get("/inventory/{item}")
    @ContinueSpan // <4>
    open fun getInventory(item: String) : Map<String, Any> { // <5>

        val counts : MutableMap<String, Any> = inventory.getStockCounts(item)
            .toMutableMap<String, Any>()
            .ifEmpty{ mutableMapOf("note" to "Not available at store") }

        counts["item"] = item

        return counts
    }
}