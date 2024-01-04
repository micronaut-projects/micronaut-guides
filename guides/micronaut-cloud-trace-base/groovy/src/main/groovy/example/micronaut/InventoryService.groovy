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

import io.opentelemetry.instrumentation.annotations.SpanAttribute
import io.opentelemetry.instrumentation.annotations.WithSpan
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer
import jakarta.inject.Singleton

import java.util.Collection
import java.util.Map
import java.util.concurrent.ConcurrentHashMap

@Singleton
class InventoryService {

    private final Tracer tracer
    private final WarehouseClient warehouse
    private final Map<String, Integer> inventory = new ConcurrentHashMap<>()

    private static final String storeName = 'my_store'

    InventoryService(Tracer tracer, WarehouseClient warehouse) { // <1>
        this.tracer    = tracer
        this.warehouse = warehouse

        inventory << [laptop:4, desktop:2, monitor:11]
    }

    Collection<String> getProductNames() {
        inventory.keySet()
    }

    @WithSpan('stock-counts') // <2>
    Map<String, Integer> getStockCounts(@SpanAttribute('inventory.item') String item) { // <3>
        def counts = [:]
        if(inventory.containsKey(item)) {
            int count = inventory[item]
            counts.'store' = count

            if(count < 10) {
                counts.'warehouse' = inWarehouse(storeName, item)
            }
        }

        counts
    }

    private int inWarehouse(String store, String item) {
        Span.current().setAttribute('inventory.store-name', store) // <4>

        warehouse.getItemCount(store, getUPC(item))
    }

    void order(String item, int count) {
        orderFromWarehouse(item, count)

        inventory[item] = count + inventory[item] ?: 0
    }

    private void orderFromWarehouse(String item, int count) {
        Span span = tracer.spanBuilder('warehouse-order') // <5>
                .setAttribute('item', item)
                .setAttribute('count', count)
                .startSpan()

        def json = [store: storeName, product: item, amount: count, upc: getUPC(item)]

        warehouse.order(json)

        span.end()
    }

    private int getUPC(String item) {
        return Math.abs(item.hashCode())
    }
}
