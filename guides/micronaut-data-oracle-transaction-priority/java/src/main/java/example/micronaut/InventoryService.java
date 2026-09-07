package example.micronaut;

import io.micronaut.transaction.annotation.OracleTransactional;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import java.util.concurrent.TimeUnit;

@Singleton
public class InventoryService {

    static final long DEMO_ITEM_ID = 1L;
    private static final String DEMO_ITEM_NAME = "Last available item";

    private final InventoryItemRepository inventoryItemRepository;

    InventoryService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Transactional
    public InventoryItem resetDemoItem() {
        InventoryItem item = new InventoryItem(DEMO_ITEM_ID, DEMO_ITEM_NAME, 1, Status.AVAILABLE);
        return inventoryItemRepository.findById(DEMO_ITEM_ID).isPresent()
            ? inventoryItemRepository.update(item)
            : inventoryItemRepository.insert(item);
    }

    @Transactional(readOnly = true)
    public InventoryItem demoItem() {
        return inventoryItemRepository.findById(DEMO_ITEM_ID)
            .orElseThrow(() -> new IllegalStateException("The demo inventory item has not been initialized"));
    }

    /**
     * A background stock reconciliation is deliberately LOW priority. It holds the item lock while
     * an intentionally slow external count is performed. If a customer checkout needs the row,
     * Oracle can roll this transaction back.
     *
     * A priority rollback is not retried automatically. A caller that wants to retry must invoke
     * this operation in a new transaction and re-read/revalidate the item first.
     */
    @OracleTransactional(priority = OracleTransactional.Priority.LOW) // <1>
    public InventoryItem reconcileStock(long holdSeconds, Runnable lockAcquired) { // <2>
        InventoryItem item = lockedDemoItem();
        // SELECT FOR UPDATE owns the row lock while the deliberately slow reconciliation runs.
        lockAcquired.run();
        sleep(holdSeconds);
        // This is the business write and the post-wait JDBC round trip. If Oracle rolled back this
        // low-priority transaction while it was waiting, ORA-63300/ORA-63302 is reported here.
        InventoryItem reconciled = new InventoryItem(item.id(), item.name(), 0, Status.RECONCILED);
        return inventoryItemRepository.update(reconciled); // <3>
    }

    @OracleTransactional(priority = OracleTransactional.Priority.HIGH) // <4>
    public InventoryItem checkout() {
        InventoryItem item = lockedDemoItem();
        InventoryItem checkedOut = new InventoryItem(item.id(), item.name(), 0, Status.CHECKED_OUT);
        return inventoryItemRepository.update(checkedOut);
    }

    private InventoryItem lockedDemoItem() {
        return inventoryItemRepository.findByIdForUpdate(DEMO_ITEM_ID)
            .orElseThrow(() -> new IllegalStateException("The demo inventory item has not been initialized"));
    }

    private static void sleep(long holdSeconds) {
        try {
            TimeUnit.SECONDS.sleep(holdSeconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("The stock reconciliation was interrupted", e);
        }
    }
}
