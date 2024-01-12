/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

@ExecuteOn(TaskExecutors.BLOCKING)
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