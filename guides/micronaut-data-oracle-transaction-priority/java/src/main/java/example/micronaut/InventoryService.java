/*
 * Copyright 2017-2026 original authors
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

import io.micronaut.transaction.annotation.OracleTransactional;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import java.time.Duration;

@Singleton
public class InventoryService {

    static final long DEMO_ITEM_ID = 1L;

    private final InventoryItemRepository inventoryItemRepository;

    InventoryService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Transactional
    public InventoryItem reset() {
        InventoryItem item = new InventoryItem(DEMO_ITEM_ID, "Last available item", 1, Status.AVAILABLE);
        return inventoryItemRepository.existsById(DEMO_ITEM_ID)
            ? inventoryItemRepository.update(item)
            : inventoryItemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public InventoryItem find() {
        return inventoryItemRepository.findById(DEMO_ITEM_ID).orElseThrow();
    }

    @OracleTransactional(priority = OracleTransactional.Priority.LOW) // <1>
    public InventoryItem reconcile(Duration countDuration) {
        InventoryItem item = inventoryItemRepository.findByIdForUpdate(DEMO_ITEM_ID).orElseThrow(); // <2>
        countStock(countDuration); // <3>
        return inventoryItemRepository.update(item.withStatus(Status.RECONCILED)); // <4>
    }

    @OracleTransactional(priority = OracleTransactional.Priority.HIGH) // <5>
    public InventoryItem checkout() {
        InventoryItem item = inventoryItemRepository.findByIdForUpdate(DEMO_ITEM_ID).orElseThrow(); // <6>
        return inventoryItemRepository.update(new InventoryItem(item.id(), item.name(), 0, Status.CHECKED_OUT));
    }

    private static void countStock(Duration countDuration) {
        try {
            Thread.sleep(countDuration); // Simulates a slow count in an external system.
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("The stock count was interrupted", e);
        }
    }
}
