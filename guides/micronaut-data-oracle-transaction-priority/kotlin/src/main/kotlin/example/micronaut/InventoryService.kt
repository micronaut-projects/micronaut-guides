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
package example.micronaut

import io.micronaut.transaction.annotation.OracleTransactional
import io.micronaut.transaction.annotation.Transactional
import jakarta.inject.Singleton
import java.time.Duration

@Singleton
open class InventoryService(private val inventoryItemRepository: InventoryItemRepository) {

    @Transactional
    open fun reset(): InventoryItem {
        val item = InventoryItem(DEMO_ITEM_ID, "Last available item", 1, Status.AVAILABLE)
        return if (inventoryItemRepository.existsById(DEMO_ITEM_ID)) {
            inventoryItemRepository.update(item)
        } else {
            inventoryItemRepository.save(item)
        }
    }

    @Transactional(readOnly = true)
    open fun find(): InventoryItem = inventoryItemRepository.findById(DEMO_ITEM_ID).orElseThrow()

    @OracleTransactional(priority = OracleTransactional.Priority.LOW) // <1>
    open fun reconcile(countDuration: Duration): InventoryItem {
        val item = inventoryItemRepository.findByIdForUpdate(DEMO_ITEM_ID).orElseThrow() // <2>
        countStock(countDuration) // <3>
        return inventoryItemRepository.update(item.copy(status = Status.RECONCILED)) // <4>
    }

    @OracleTransactional(priority = OracleTransactional.Priority.HIGH) // <5>
    open fun checkout(): InventoryItem {
        val item = inventoryItemRepository.findByIdForUpdate(DEMO_ITEM_ID).orElseThrow() // <6>
        return inventoryItemRepository.update(item.copy(availableQuantity = 0, status = Status.CHECKED_OUT))
    }

    private fun countStock(countDuration: Duration) {
        try {
            Thread.sleep(countDuration) // Simulates a slow count in an external system.
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            throw IllegalStateException("The stock count was interrupted", e)
        }
    }

    companion object {
        const val DEMO_ITEM_ID = 1L
    }
}
