package example.micronaut

import example.micronaut.domain.Account
import io.micronaut.data.exceptions.DataIntegrityViolationException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class AccountTest extends Specification {

    @Inject
    AccountRepository repository

    void 'reservation updates multiple fields'() {
        given:
        Account account = repository.save(new Account('Checking', 100L, 50L))

        when:
        long updated = repository.reserveIncrementBalanceAndDecrementCredit(account.id(), 25L, 10L)

        then:
        updated == 1L
        Account found = repository.findById(account.id()).orElseThrow()
        found.balance() == 125L
        found.credit() == 40L
    }

    void 'reservation constraint failure is mapped'() {
        given:
        Account account = repository.save(new Account('Checking', 100L, 50L))

        when:
        repository.reserveIncrementBalanceAndDecrementCredit(account.id(), 0L, 1000L)

        then:
        thrown(DataIntegrityViolationException)
    }
}
