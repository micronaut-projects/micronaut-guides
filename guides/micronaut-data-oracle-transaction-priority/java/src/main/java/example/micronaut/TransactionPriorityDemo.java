package example.micronaut;

import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/** Coordinates two independent transactions used by the HTTP demo. */
@Singleton
public class TransactionPriorityDemo {

    private final AccountService accountService;
    private final ExecutorService blockingExecutor;
    private final List<DemoEvent> events = new ArrayList<>();

    private volatile OperationState lowState = OperationState.IDLE;
    private volatile OperationState highState = OperationState.IDLE;
    private volatile CountDownLatch lowLockAcquired = new CountDownLatch(0);

    TransactionPriorityDemo(AccountService accountService,
                            @Named(TaskExecutors.BLOCKING) ExecutorService blockingExecutor) {
        this.accountService = accountService;
        this.blockingExecutor = blockingExecutor;
    }

    public synchronized DemoSnapshot reset() {
        ensureIdle();
        events.clear();
        lowState = OperationState.IDLE;
        highState = OperationState.IDLE;
        Account account = accountService.resetDemoAccount();
        event("RESET", "Demo account reset: available for checkout");
        return snapshot(account);
    }

    public StartResult startLow(long holdSeconds) {
        synchronized (this) {
            if (lowState == OperationState.STARTING || lowState == OperationState.LOCK_HELD) {
                return new StartResult(false, snapshot(), "A LOW-priority transaction is already running");
            }
            if (highState == OperationState.WAITING) {
                return new StartResult(false, snapshot(), "A HIGH-priority transaction is already waiting");
            }
            if (holdSeconds < 1 || holdSeconds > 120) {
                return new StartResult(false, snapshot(), "holdSeconds must be between 1 and 120");
            }
            CountDownLatch lockSignal = new CountDownLatch(1);
            lowLockAcquired = lockSignal;
            lowState = OperationState.STARTING;
            event("LOW", "Starting LOW-priority transaction");
            blockingExecutor.submit(() -> runLow(holdSeconds, lockSignal));
        }
        try {
            if (!lowLockAcquired.await(10, TimeUnit.SECONDS)) {
                lowState = OperationState.FAILED;
                event("LOW", "Timed out waiting for the LOW-priority transaction to lock the row");
                return new StartResult(false, snapshot(), "LOW transaction did not acquire the row lock");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new StartResult(false, snapshot(), "Interrupted while starting LOW transaction");
        }
        return new StartResult(true, snapshot(), "LOW-priority transaction holds the row lock");
    }

    public StartResult startHigh() {
        synchronized (this) {
            if (lowState != OperationState.LOCK_HELD) {
                return new StartResult(false, snapshot(), "Start LOW first and wait for it to hold the row lock");
            }
            if (highState == OperationState.WAITING) {
                return new StartResult(false, snapshot(), "A HIGH-priority transaction is already waiting");
            }
            highState = OperationState.WAITING;
            event("HIGH", "HIGH-priority checkout is waiting for the row lock");
            blockingExecutor.submit(this::runHigh);
        }
        return new StartResult(true, snapshot(), "HIGH-priority checkout started; poll GET /demo");
    }

    public DemoSnapshot snapshot() {
        return snapshot(accountService.demoAccount());
    }

    private void runLow(long holdSeconds, CountDownLatch lockSignal) {
        try {
            accountService.holdLowPriorityLock(holdSeconds, () -> {
                lowState = OperationState.LOCK_HELD;
                event("LOW", "LOW-priority transaction acquired SELECT FOR UPDATE lock");
                lockSignal.countDown();
            });
            lowState = OperationState.COMMITTED;
            event("LOW", "LOW-priority transaction committed (priority rollback was not triggered)");
        } catch (Exception e) {
            lowState = OperationState.ROLLED_BACK;
            event("LOW", "LOW-priority transaction ended with rollback: " + conciseMessage(e));
            lockSignal.countDown();
        }
    }

    private void runHigh() {
        try {
            accountService.completeHighPriorityCheckout();
            highState = OperationState.COMMITTED;
            event("HIGH", "HIGH-priority checkout committed");
        } catch (Exception e) {
            highState = OperationState.FAILED;
            event("HIGH", "HIGH-priority checkout failed: " + conciseMessage(e));
        }
    }

    private synchronized DemoSnapshot snapshot(Account account) {
        return new DemoSnapshot(new AccountView(account), lowState, highState, List.copyOf(events));
    }

    private synchronized void event(String operation, String message) {
        events.add(new DemoEvent(Instant.now(), operation, message));
    }

    private void ensureIdle() {
        if (lowState == OperationState.STARTING || lowState == OperationState.LOCK_HELD || highState == OperationState.WAITING) {
            throw new IllegalStateException("Cannot reset while a demo transaction is active");
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
    record StartResult(boolean started, DemoSnapshot snapshot, String message) { }

    @Serdeable
    record DemoSnapshot(AccountView account, OperationState low, OperationState high, List<DemoEvent> events) { }

    @Serdeable
    record AccountView(Long id, String name, Double balance, Status status) {
        AccountView(Account account) {
            this(account.getId(), account.getName(), account.getBalance(), account.getStatus());
        }
    }

    @Serdeable
    record DemoEvent(Instant at, String operation, String message) { }
}
