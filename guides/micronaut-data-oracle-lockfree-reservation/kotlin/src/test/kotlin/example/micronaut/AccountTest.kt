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

import example.micronaut.domain.Account
import io.micronaut.data.exceptions.DataIntegrityViolationException
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false) // <1>
class AccountTest {

    @Inject
    lateinit var repository: AccountRepository

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun reservationUpdatesMultipleFields() {
        val account = repository.save(Account(name = "Checking", balance = 100, credit = 50))

        val updated = repository.reserveIncrementBalanceAndDecrementCredit(account.id!!, 25, 10) // <2>

        assertEquals(1L, updated)
        val found = repository.findById(account.id!!).orElseThrow()
        assertEquals(125L, found.balance)
        assertEquals(40L, found.credit)
    }

    @Test
    fun reservationConstraintFailureIsMapped() {
        val account = repository.save(Account(name = "Checking", balance = 100, credit = 50))

        assertThrows(DataIntegrityViolationException::class.java) {
            repository.reserveIncrementBalanceAndDecrementCredit(account.id!!, 0, 1000) // <3>
        }

        val found = repository.findById(account.id!!).orElseThrow()
        assertEquals(50L, found.credit) // <4>
    }

    @Test
    fun reservationConstraintFailureRespondsWithConflict() {
        val account = repository.save(Account(name = "Checking", balance = 100, credit = 50))

        val e = assertThrows(HttpClientResponseException::class.java) {
            httpClient.toBlocking().exchange<Any, Any>(
                HttpRequest.POST("/accounts/${account.id}/reserve?balance=0&credit=1000", ""))
        }

        assertEquals(HttpStatus.CONFLICT, e.status) // <5>
    }
}
