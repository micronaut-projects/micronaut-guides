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
package example.micronaut;

import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.tracing.annotation.ContinueSpan;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/store")
public class StoreController {
    private final InventoryService inventory;

    public StoreController(InventoryService inventory) {
        this.inventory = inventory;
    }

    @Post("/order")
    @Status(HttpStatus.CREATED)
    @NewSpan("store.order") // <1>
    public void order(@SpanTag("order.item") String item, @SpanTag int count) { // <2>
        inventory.order(item, count);
    }

    @Get("/inventory") // <3>
    public List<Map<String, Object>> getInventory() {
        List<Map<String, Object>> currentInventory = new ArrayList<>();
        for (String product: inventory.getProductNames()) {
            currentInventory.add(getInventory(product));
        }
        return currentInventory;
    }

    @Get("/inventory/{item}")
    @ContinueSpan // <4>
    public Map<String, Object> getInventory(@SpanTag("item") String item) { // <5>
        Map<String, Object> counts = new HashMap<>(inventory.getStockCounts(item));
        if(counts.isEmpty()) {
            counts.put("note", "Not available at store");
        }

        counts.put("item", item);

        return counts;
    }
}
