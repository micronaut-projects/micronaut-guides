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

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.transaction.TransactionOperations
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.SQLException
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@MicronautTest(transactional = false) // <1>
class InventoryControllerTest {

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Inject
    lateinit var transactionOperations: TransactionOperations<Connection>

    lateinit var client: BlockingHttpClient

    @BeforeEach
    fun reset() {
        client = httpClient.toBlocking()
        client.retrieve(HttpRequest.POST("/inventory/reset", ""), InventoryItem::class.java)
    }

    @Test
    fun reconciliationCommitsWithoutContention() {
        val item = client.retrieve(HttpRequest.POST("/inventory/reconcile?countSeconds=1", ""), InventoryItem::class.java)

        assertEquals(Status.RECONCILED, item.status)
        assertEquals(1, item.availableQuantity)
    }

    @Test
    fun highPriorityCheckoutRollsBackLowPriorityReconciliation() {
        val reconciliation = CompletableFuture.supplyAsync {
            assertThrows(HttpClientResponseException::class.java) {
                client.exchange<Any, Any>(HttpRequest.POST("/inventory/reconcile?countSeconds=8", "")) // <2>
            }
        }
        awaitItemLocked() // <3>

        val checkedOut = client.retrieve(HttpRequest.POST("/inventory/checkout", ""), InventoryItem::class.java) // <4>
        assertEquals(Status.CHECKED_OUT, checkedOut.status)

        val rolledBack = reconciliation.get(15, TimeUnit.SECONDS)
        assertEquals(HttpStatus.CONFLICT, rolledBack.status) // <5>
        assertTrue(rolledBack.response.getBody(String::class.java).orElse("")
            .contains("Oracle rolled back this operation in favor of a higher-priority transaction")) // <6>

        val item = client.retrieve(HttpRequest.GET<Any>("/inventory"), InventoryItem::class.java)
        assertEquals(Status.CHECKED_OUT, item.status) // <7>
        assertEquals(0, item.availableQuantity)
    }

    @Test
    fun reconciliationRejectsInvalidCountDuration() {
        val e = assertThrows(HttpClientResponseException::class.java) {
            client.exchange<Any, Any>(HttpRequest.POST("/inventory/reconcile?countSeconds=0", ""))
        }

        assertEquals(HttpStatus.BAD_REQUEST, e.status) // <8>
    }

    private fun awaitItemLocked() {
        val deadline = System.nanoTime() + Duration.ofSeconds(10).toNanos()
        while (System.nanoTime() < deadline) {
            if (isItemLocked()) {
                return
            }
            Thread.sleep(100)
        }
        fail<Unit>("The reconciliation did not lock the inventory item")
    }

    private fun isItemLocked(): Boolean = transactionOperations.executeWrite { status ->
        val sql = "SELECT id FROM inventory_item WHERE id = ? FOR UPDATE NOWAIT"
        try {
            status.connection.prepareStatement(sql).use { statement ->
                statement.setLong(1, InventoryService.DEMO_ITEM_ID)
                statement.executeQuery().close()
                false
            }
        } catch (e: SQLException) {
            if (e.errorCode != ORA_RESOURCE_BUSY) {
                throw IllegalStateException(e)
            }
            true
        }
    }

    companion object {
        private const val ORA_RESOURCE_BUSY = 54
    }
}
