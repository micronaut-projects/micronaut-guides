package example.micronaut;

import example.micronaut.domain.Account;
import io.micronaut.data.exceptions.DataIntegrityViolationException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
class AccountTest {

    @Inject
    AccountRepository repository;

    @Test
    void reservationUpdatesMultipleFields() {
        Account account = repository.save(new Account("Checking", 100L, 50L));

        long updated = repository.reserveIncrementBalanceAndDecrementCredit(account.id(), 25L, 10L);

        assertEquals(1L, updated);
        Account found = repository.findById(account.id()).orElseThrow();
        assertEquals(125L, found.balance());
        assertEquals(40L, found.credit());
    }

    @Test
    void reservationConstraintFailureIsMapped() {
        Account account = repository.save(new Account("Checking", 100L, 50L));

        assertThrows(DataIntegrityViolationException.class,
            () -> repository.reserveIncrementBalanceAndDecrementCredit(account.id(), 0L, 1000L));
    }
}
