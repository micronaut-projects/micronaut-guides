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

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.transaction.TransactionOperations;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@MicronautTest(transactional = false) // <1>
class InventoryControllerTest {

    private static final int ORA_RESOURCE_BUSY = 54;

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Inject
    TransactionOperations<Connection> transactionOperations;

    BlockingHttpClient client;

    @BeforeEach
    void reset() {
        client = httpClient.toBlocking();
        client.retrieve(HttpRequest.POST("/inventory/reset", null), InventoryItem.class);
    }

    @Test
    void reconciliationCommitsWithoutContention() {
        InventoryItem item = client.retrieve(HttpRequest.POST("/inventory/reconcile?countSeconds=1", null), InventoryItem.class);

        assertEquals(Status.RECONCILED, item.status());
        assertEquals(1, item.availableQuantity());
    }

    @Test
    void highPriorityCheckoutRollsBackLowPriorityReconciliation() throws Exception {
        CompletableFuture<HttpClientResponseException> reconciliation = CompletableFuture.supplyAsync(() ->
            assertThrows(HttpClientResponseException.class, () ->
                client.exchange(HttpRequest.POST("/inventory/reconcile?countSeconds=8", null)))); // <2>
        awaitItemLocked(); // <3>

        InventoryItem checkedOut = client.retrieve(HttpRequest.POST("/inventory/checkout", null), InventoryItem.class); // <4>
        assertEquals(Status.CHECKED_OUT, checkedOut.status());

        HttpClientResponseException rolledBack = reconciliation.get(15, TimeUnit.SECONDS);
        assertEquals(HttpStatus.CONFLICT, rolledBack.getStatus()); // <5>

        InventoryItem item = client.retrieve(HttpRequest.GET("/inventory"), InventoryItem.class);
        assertEquals(Status.CHECKED_OUT, item.status()); // <6>
        assertEquals(0, item.availableQuantity());
    }

    private void awaitItemLocked() throws InterruptedException {
        long deadline = System.nanoTime() + Duration.ofSeconds(10).toNanos();
        while (System.nanoTime() < deadline) {
            if (isItemLocked()) {
                return;
            }
            Thread.sleep(100);
        }
        fail("The reconciliation did not lock the inventory item");
    }

    private boolean isItemLocked() {
        return transactionOperations.executeWrite(status -> {
            String sql = "SELECT id FROM inventory_item WHERE id = ? FOR UPDATE NOWAIT";
            try (PreparedStatement statement = status.getConnection().prepareStatement(sql)) {
                statement.setLong(1, InventoryService.DEMO_ITEM_ID);
                statement.executeQuery().close();
                return false;
            } catch (SQLException e) {
                if (e.getErrorCode() == ORA_RESOURCE_BUSY) {
                    return true;
                }
                throw new IllegalStateException(e);
            }
        });
    }
}
