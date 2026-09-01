package example.micronaut;

import io.micronaut.transaction.annotation.OracleTransactional;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Singleton;

import java.util.concurrent.TimeUnit;

@Singleton
public class AccountService {

    static final long DEMO_ACCOUNT_ID = 1L;

    private final AccountRepository accountRepository;

    AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Account resetDemoAccount() {
        Account account = accountRepository.findById(DEMO_ACCOUNT_ID).orElse(null);
        boolean newAccount = account == null;
        if (account == null) {
            account = new Account();
            account.setId(DEMO_ACCOUNT_ID);
        }
        account.setName("Last available item");
        account.setBalance(0D);
        account.setStatus(Status.ACTIVE);
        return newAccount ? accountRepository.insert(account) : accountRepository.update(account);
    }

    @Transactional(readOnly = true)
    public Account demoAccount() {
        return accountRepository.findById(DEMO_ACCOUNT_ID)
            .orElseThrow(() -> new IllegalStateException("The demo account has not been initialized"));
    }

    /**
     * Locks the row as LOW priority and keeps the transaction open. The second update gives Oracle
     * a JDBC round trip at which an automatic priority rollback can be reported.
     */
    // <1>
    @OracleTransactional(priority = OracleTransactional.Priority.LOW)
    public Account holdLowPriorityLock(long holdSeconds, Runnable lockAcquired) {
        // <2>
        Account account = lockedDemoAccount();
        account.setBalance(10D);
        accountRepository.update(account);
        lockAcquired.run();
        sleep(holdSeconds);
        account.setStatus(Status.INACTIVE);
        // <3>
        return accountRepository.update(account);
    }

    // <4>
    @OracleTransactional(priority = OracleTransactional.Priority.HIGH)
    public Account completeHighPriorityCheckout() {
        Account account = lockedDemoAccount();
        account.setBalance(100D);
        account.setStatus(Status.ACTIVE);
        return accountRepository.update(account);
    }

    private Account lockedDemoAccount() {
        return accountRepository.findByIdForUpdate(DEMO_ACCOUNT_ID)
            .orElseThrow(() -> new IllegalStateException("The demo account has not been initialized"));
    }

    private static void sleep(long holdSeconds) {
        try {
            TimeUnit.SECONDS.sleep(holdSeconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("The LOW-priority transaction was interrupted", e);
        }
    }
}
