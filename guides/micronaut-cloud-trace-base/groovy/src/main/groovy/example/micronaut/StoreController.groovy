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

import java.util.ArrayList
import java.util.HashMap
import java.util.List
import java.util.Map

@ExecuteOn(TaskExecutors.IO)
@Controller('/store')
class StoreController {
    private final InventoryService inventory

    StoreController(InventoryService inventory) {
        this.inventory = inventory
    }

    @Post('/order')
    @Status(HttpStatus.CREATED)
    @NewSpan('store.order') // <1>
    void order(@SpanTag('order.item') String item, @SpanTag int count) { // <2>
        inventory.order(item, count)
    }

    @Get('/inventory') // <3>
    List<Map<String, Object>> getInventory() {
        List<Map<String, Object>> currentInventory = []

        inventory.productNames.each{ product ->
            currentInventory << getInventory(product)
        }

        currentInventory
    }

    @Get('/inventory/{item}')
    @ContinueSpan // <4>
    Map<String, Object> getInventory(@SpanTag('item') String item) { // <5>
        def counts = inventory.getStockCounts(item)
        if(!counts) {
            counts.'note' = "Not available at store"
        }

        counts.'item' = item

        counts
    }
}
