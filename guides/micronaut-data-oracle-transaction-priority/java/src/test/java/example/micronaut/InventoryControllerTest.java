package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// <1>
@MicronautTest(transactional = false)
class InventoryControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void highPriorityCheckoutCanProtectTheLastItem() throws Exception {
        InventoryController.InventorySnapshot reset = httpClient.toBlocking()
            .retrieve(HttpRequest.POST("/inventory/reset", null), InventoryController.InventorySnapshot.class);
        assertEquals(InventoryController.OperationState.IDLE, reset.reconciliation());
        assertEquals(1, reset.item().availableQuantity());

        // The test sends the long-running request asynchronously so the next request can contend for its lock.
        CompletableFuture<HttpStatus> reconciliation = CompletableFuture.supplyAsync(() -> {
            try {
                httpClient.toBlocking().exchange(
                    // <2>
                    HttpRequest.POST("/inventory/reconcile?holdSeconds=8", null),
                    InventoryController.InventorySnapshot.class);
                return HttpStatus.OK;
            } catch (HttpClientResponseException e) {
                // The controller reports the expected priority rollback as HTTP 409.
                return e.getStatus();
            }
        });

        InventoryController.InventorySnapshot locked = waitForState(
            InventoryController.OperationState.LOCK_HELD);
        assertEquals(InventoryController.OperationState.LOCK_HELD, locked.reconciliation());

        HttpResponse<InventoryController.InventorySnapshot> checkout = httpClient.toBlocking().exchange(
            // <3>
            HttpRequest.POST("/inventory/checkout", null), InventoryController.InventorySnapshot.class);
        assertEquals(HttpStatus.OK, checkout.getStatus());

        assertEquals(HttpStatus.CONFLICT, reconciliation.get(15, TimeUnit.SECONDS));

        InventoryController.InventorySnapshot snapshot = waitForCompletion();
        assertEquals(InventoryController.OperationState.COMMITTED, snapshot.checkout());
        assertEquals(InventoryController.OperationState.ROLLED_BACK, snapshot.reconciliation(),
            () -> "Priority rollback was not observed. Check that priority-txns.sql ran in FREEPDB1. Events: "
                + snapshot.events());
        assertEquals(0, snapshot.item().availableQuantity());
        assertEquals(Status.CHECKED_OUT, snapshot.item().status());
    }

    private InventoryController.InventorySnapshot waitForState(
        InventoryController.OperationState expected) throws InterruptedException {
        long deadline = System.nanoTime() + Duration.ofSeconds(10).toNanos();
        InventoryController.InventorySnapshot snapshot = null;
        while (System.nanoTime() < deadline) {
            snapshot = status();
            if (snapshot.reconciliation() == expected) {
                return snapshot;
            }
            Thread.sleep(100);
        }
        assertTrue(snapshot != null);
        return snapshot;
    }

    private InventoryController.InventorySnapshot waitForCompletion() throws InterruptedException {
        long deadline = System.nanoTime() + Duration.ofSeconds(15).toNanos();
        InventoryController.InventorySnapshot snapshot = null;
        while (System.nanoTime() < deadline) {
            snapshot = status();
            if (snapshot.checkout() == InventoryController.OperationState.COMMITTED
                && snapshot.reconciliation() == InventoryController.OperationState.ROLLED_BACK) {
                return snapshot;
            }
            Thread.sleep(100);
        }
        assertTrue(snapshot != null);
        return snapshot;
    }

    private InventoryController.InventorySnapshot status() {
        return httpClient.toBlocking()
            .retrieve(HttpRequest.GET("/inventory"), InventoryController.InventorySnapshot.class);
    }
}
