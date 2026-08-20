package example.micronaut

import example.micronaut.domain.Account
import io.micronaut.data.exceptions.DataIntegrityViolationException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

@MicronautTest
class AccountTest {

    @Inject
    lateinit var repository: AccountRepository

    @Test
    fun reservationUpdatesMultipleFields() {
        val account = repository.save(Account(name = "Checking", balance = 100, credit = 50))

        val updated = repository.reserveIncrementBalanceAndDecrementCredit(account.id!!, 25, 10)

        assertEquals(1L, updated)
        val found = repository.findById(account.id!!).orElseThrow()
        assertEquals(125L, found.balance)
        assertEquals(40L, found.credit)
    }

    @Test
    fun reservationConstraintFailureIsMapped() {
        val account = repository.save(Account(name = "Checking", balance = 100, credit = 50))

        assertThrows(DataIntegrityViolationException::class.java) {
            repository.reserveIncrementBalanceAndDecrementCredit(account.id!!, 0, 1000)
        }
    }
}
