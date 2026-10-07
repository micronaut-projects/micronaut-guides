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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest(transactional = false) // <1>
class AccountTest {

    @Inject
    lateinit var repository: AccountRepository

    @Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @Test
    fun depositIncrementsBalance() {
        val account = repository.save(Account(name = "Checking", balance = 100))

        val updated = repository.reserveIncrementBalance(account.id!!, 25) // <2>

        assertEquals(1L, updated)
        val found = repository.findById(account.id!!).orElseThrow()
        assertEquals(125L, found.balance)
    }

    @Test
    fun withdrawalBeyondBalanceIsRejected() {
        val account = repository.save(Account(name = "Checking", balance = 100))

        assertThrows(DataIntegrityViolationException::class.java) {
            repository.reserveDecrementBalance(account.id!!, 1000) // <3>
        }

        val found = repository.findById(account.id!!).orElseThrow()
        assertEquals(100L, found.balance) // <4>
    }

    @Test
    fun withdrawalBeyondBalanceRespondsWithConflict() {
        val account = repository.save(Account(name = "Checking", balance = 100))

        val e = assertThrows(HttpClientResponseException::class.java) {
            httpClient.toBlocking().exchange<Any, Any>(
                HttpRequest.POST("/accounts/${account.id}/withdraw?amount=1000", ""))
        }

        assertEquals(HttpStatus.CONFLICT, e.status) // <5>
        assertTrue(e.response.getBody(String::class.java).orElse("")
            .contains("The operation violates an account constraint")) // <6>
    }

    @Test
    fun accountIsCreatedDepositedAndWithdrawnOverHttp() {
        val client = httpClient.toBlocking()

        val created = client.exchange(
            HttpRequest.POST("/accounts", Account(name = "Savings", balance = 100)), Account::class.java) // <7>
        assertEquals(HttpStatus.CREATED, created.status)
        val account = created.body()!!

        val deposited = client.retrieve(
            HttpRequest.POST("/accounts/${account.id}/deposit?amount=25", ""), Account::class.java) // <8>
        assertEquals(125L, deposited.balance)

        val withdrawn = client.retrieve(
            HttpRequest.POST("/accounts/${account.id}/withdraw?amount=50", ""), Account::class.java)
        assertEquals(75L, withdrawn.balance)
    }

    @Test
    fun depositForUnknownAccountRespondsWithNotFound() {
        val e = assertThrows(HttpClientResponseException::class.java) {
            httpClient.toBlocking().exchange<Any, Any>(
                HttpRequest.POST("/accounts/-1/deposit?amount=1", ""))
        }

        assertEquals(HttpStatus.NOT_FOUND, e.status) // <9>
    }
}
