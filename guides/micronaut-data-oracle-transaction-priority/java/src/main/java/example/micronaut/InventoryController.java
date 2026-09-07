package example.micronaut;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.serde.annotation.Serdeable;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@ExecuteOn(TaskExecutors.BLOCKING)
@Controller("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final List<DemoEvent> events = new ArrayList<>();

    private volatile OperationState reconciliationState = OperationState.IDLE;
    private volatile OperationState checkoutState = OperationState.IDLE;

    InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Post("/reset")
    public synchronized InventorySnapshot reset() {
        ensureIdle();
        events.clear();
        reconciliationState = OperationState.IDLE;
        checkoutState = OperationState.IDLE;
        InventoryItem item = inventoryService.resetDemoItem();
        event("RESET", "Inventory reset: one item is available");
        return snapshot(item);
    }

    /**
     * Models a slow, non-critical stock reconciliation. The request stays open while the
     * reconciliation transaction holds the row lock; a second HTTP request can run checkout.
     */
    @Post("/reconcile")
    public HttpResponse<InventorySnapshot> reconcile(
        @QueryValue(defaultValue = "20") long holdSeconds) {
        synchronized (this) {
            if (reconciliationState == OperationState.STARTING || reconciliationState == OperationState.LOCK_HELD) {
                return conflict("A stock reconciliation is already running");
            }
            if (checkoutState == OperationState.WAITING) {
                return conflict("A customer checkout is already waiting");
            }
            if (holdSeconds < 1 || holdSeconds > 120) {
                return conflict("holdSeconds must be between 1 and 120");
            }
            reconciliationState = OperationState.STARTING;
            event("RECONCILIATION", "Starting LOW-priority stock reconciliation");
        }

        try {
            InventoryItem item = inventoryService.reconcileStock(holdSeconds, () -> {
                synchronized (this) {
                    reconciliationState = OperationState.LOCK_HELD;
                    event("RECONCILIATION", "Stock reconciliation acquired the item lock");
                }
            });
            synchronized (this) {
                reconciliationState = OperationState.COMMITTED;
                event("RECONCILIATION", "Stock reconciliation committed");
            }
            return HttpResponse.ok(snapshot(item));
        } catch (Exception e) {
            synchronized (this) {
                reconciliationState = OperationState.ROLLED_BACK;
                event("RECONCILIATION", "Stock reconciliation rolled back: " + conciseMessage(e));
            }
            return HttpResponse.status(HttpStatus.CONFLICT).body(snapshot());
        }
    }

    /**
     * Models the critical customer operation. The HTTP request waits for the row lock, so it can
     * contend with the reconciliation request without an application-managed executor.
     */
    @Post("/checkout")
    public HttpResponse<InventorySnapshot> checkout() {
        synchronized (this) {
            if (reconciliationState != OperationState.LOCK_HELD) {
                return conflict("Start reconciliation first and wait for it to hold the item lock");
            }
            if (checkoutState == OperationState.WAITING) {
                return conflict("A customer checkout is already waiting");
            }
            checkoutState = OperationState.WAITING;
            event("CHECKOUT", "Customer checkout is waiting for the item lock");
        }

        try {
            InventoryItem item = inventoryService.checkout();
            synchronized (this) {
                checkoutState = OperationState.COMMITTED;
                event("CHECKOUT", "Customer checkout committed");
            }
            return HttpResponse.ok(snapshot(item));
        } catch (Exception e) {
            synchronized (this) {
                checkoutState = OperationState.FAILED;
                event("CHECKOUT", "Customer checkout failed: " + conciseMessage(e));
            }
            return HttpResponse.serverError(snapshot());
        }
    }

    @Get
    public InventorySnapshot status() {
        return snapshot();
    }

    private InventorySnapshot snapshot() {
        return snapshot(inventoryService.demoItem());
    }

    private synchronized InventorySnapshot snapshot(InventoryItem item) {
        return new InventorySnapshot(new ItemView(item), reconciliationState, checkoutState, List.copyOf(events));
    }

    private synchronized void event(String operation, String message) {
        events.add(new DemoEvent(Instant.now(), operation, message));
    }

    private synchronized HttpResponse<InventorySnapshot> conflict(String message) {
        event("DEMO", message);
        return HttpResponse.status(HttpStatus.CONFLICT).body(snapshot());
    }

    private void ensureIdle() {
        if (reconciliationState == OperationState.STARTING
            || reconciliationState == OperationState.LOCK_HELD
            || checkoutState == OperationState.WAITING) {
            throw new IllegalStateException("Cannot reset while an inventory operation is active");
        }
    }

    private static String conciseMessage(Exception e) {
        Throwable cause = e;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getClass().getSimpleName() + ": " + cause.getMessage();
    }

    enum OperationState { IDLE, STARTING, LOCK_HELD, WAITING, COMMITTED, ROLLED_BACK, FAILED }

    @Serdeable
    record InventorySnapshot(ItemView item,
                             OperationState reconciliation,
                             OperationState checkout,
                             List<DemoEvent> events) {
    }

    @Serdeable
    record ItemView(Long id, String name, int availableQuantity, Status status) {
        ItemView(InventoryItem item) {
            this(item.id(), item.name(), item.availableQuantity(), item.status());
        }
    }

    @Serdeable
    record DemoEvent(Instant at, String operation, String message) {
    }
}
