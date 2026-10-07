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
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.transaction.TransactionOperations
import jakarta.inject.Inject
import spock.lang.Specification

import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.time.Duration
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

@MicronautTest(transactional = false) // <1>
class InventoryControllerSpec extends Specification {

    private static final int ORA_RESOURCE_BUSY = 54

    @Inject
    @Client('/')
    HttpClient httpClient

    @Inject
    TransactionOperations<Connection> transactionOperations

    BlockingHttpClient client

    void setup() {
        client = httpClient.toBlocking()
        client.retrieve(HttpRequest.POST('/inventory/reset', null), InventoryItem)
    }

    void 'reconciliation commits without contention'() {
        when:
        InventoryItem item = client.retrieve(HttpRequest.POST('/inventory/reconcile?countSeconds=1', null), InventoryItem)

        then:
        item.status == Status.RECONCILED
        item.availableQuantity == 1
    }

    void 'high priority checkout rolls back low priority reconciliation'() {
        given:
        CompletableFuture<HttpClientResponseException> reconciliation = CompletableFuture.supplyAsync {
            try {
                client.exchange(HttpRequest.POST('/inventory/reconcile?countSeconds=8', null)) // <2>
                return null
            } catch (HttpClientResponseException e) {
                return e
            }
        }
        awaitItemLocked() // <3>

        when:
        InventoryItem checkedOut = client.retrieve(HttpRequest.POST('/inventory/checkout', null), InventoryItem) // <4>

        then:
        checkedOut.status == Status.CHECKED_OUT

        when:
        HttpClientResponseException rolledBack = reconciliation.get(15, TimeUnit.SECONDS)

        then:
        rolledBack != null
        rolledBack.status == HttpStatus.CONFLICT // <5>
        rolledBack.response.getBody(String).orElse('')
                .contains('Oracle rolled back this operation in favor of a higher-priority transaction') // <6>

        when:
        InventoryItem item = client.retrieve(HttpRequest.GET('/inventory'), InventoryItem)

        then:
        item.status == Status.CHECKED_OUT // <7>
        item.availableQuantity == 0
    }

    void 'reconciliation rejects invalid count duration'() {
        when:
        client.exchange(HttpRequest.POST('/inventory/reconcile?countSeconds=0', null))

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.BAD_REQUEST // <8>
    }

    private void awaitItemLocked() {
        long deadline = System.nanoTime() + Duration.ofSeconds(10).toNanos()
        while (System.nanoTime() < deadline) {
            if (isItemLocked()) {
                return
            }
            Thread.sleep(100)
        }
        throw new AssertionError('The reconciliation did not lock the inventory item')
    }

    private boolean isItemLocked() {
        transactionOperations.executeWrite { status ->
            String sql = 'SELECT id FROM inventory_item WHERE id = ? FOR UPDATE NOWAIT'
            PreparedStatement statement = status.connection.prepareStatement(sql)
            try {
                statement.setLong(1, InventoryService.DEMO_ITEM_ID)
                statement.executeQuery().close()
                return false
            } catch (SQLException e) {
                if (e.errorCode == ORA_RESOURCE_BUSY) {
                    return true
                }
                throw new IllegalStateException(e)
            } finally {
                statement.close()
            }
        }
    }
}
