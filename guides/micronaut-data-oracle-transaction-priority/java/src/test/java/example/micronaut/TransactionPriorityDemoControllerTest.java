package example.micronaut;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// <1>
@MicronautTest(transactional = false)
class TransactionPriorityDemoControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void highPriorityTransactionCanCompleteAfterLowPriorityLockIsReleased() throws InterruptedException {
        TransactionPriorityDemo.DemoSnapshot reset = httpClient.toBlocking()
            .retrieve(HttpRequest.POST("/demo/reset", null), TransactionPriorityDemo.DemoSnapshot.class);
        assertEquals(TransactionPriorityDemo.OperationState.IDLE, reset.low());
        assertEquals(0D, reset.account().balance());

        HttpResponse<TransactionPriorityDemo.StartResult> low = httpClient.toBlocking().exchange(
            // <2>
            HttpRequest.POST("/demo/low?holdSeconds=8", null), TransactionPriorityDemo.StartResult.class);
        assertEquals(HttpStatus.ACCEPTED, low.getStatus());
        assertEquals(TransactionPriorityDemo.OperationState.LOCK_HELD, low.body().snapshot().low());

        HttpResponse<TransactionPriorityDemo.StartResult> high = httpClient.toBlocking().exchange(
            HttpRequest.POST("/demo/high", null), TransactionPriorityDemo.StartResult.class);
        assertEquals(HttpStatus.ACCEPTED, high.getStatus());

        TransactionPriorityDemo.DemoSnapshot snapshot = waitForCompletion();
        assertEquals(TransactionPriorityDemo.OperationState.COMMITTED, snapshot.high());
        // <3>
        assertEquals(TransactionPriorityDemo.OperationState.ROLLED_BACK, snapshot.low(),
            () -> "Priority rollback was not observed. Check that priority-txns.sql ran in FREEPDB1. Events: " + snapshot.events());
        assertEquals(100D, snapshot.account().balance());
    }

    private TransactionPriorityDemo.DemoSnapshot waitForCompletion() throws InterruptedException {
        long deadline = System.nanoTime() + Duration.ofSeconds(15).toNanos();
        TransactionPriorityDemo.DemoSnapshot snapshot = null;
        while (System.nanoTime() < deadline) {
            snapshot = httpClient.toBlocking().retrieve(HttpRequest.GET("/demo"), TransactionPriorityDemo.DemoSnapshot.class);
            if (snapshot.high() == TransactionPriorityDemo.OperationState.COMMITTED
                && snapshot.low() == TransactionPriorityDemo.OperationState.ROLLED_BACK) {
                return snapshot;
            }
            Thread.sleep(100);
        }
        assertTrue(snapshot != null);
        return snapshot;
    }
}
