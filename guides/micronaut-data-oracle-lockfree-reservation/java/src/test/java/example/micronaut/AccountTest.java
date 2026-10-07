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
package example.micronaut;

import example.micronaut.domain.Account;
import io.micronaut.data.exceptions.DataIntegrityViolationException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(transactional = false) // <1>
class AccountTest {

    @Inject
    AccountRepository repository;

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void depositIncrementsBalance() {
        Account account = repository.save(new Account(null, "Checking", 100L));

        long updated = repository.reserveIncrementBalance(account.id(), 25L); // <2>

        assertEquals(1L, updated);
        Account found = repository.findById(account.id()).orElseThrow();
        assertEquals(125L, found.balance());
    }

    @Test
    void withdrawalBeyondBalanceIsRejected() {
        Account account = repository.save(new Account(null, "Checking", 100L));

        assertThrows(DataIntegrityViolationException.class,
            () -> repository.reserveDecrementBalance(account.id(), 1000L)); // <3>

        Account found = repository.findById(account.id()).orElseThrow();
        assertEquals(100L, found.balance()); // <4>
    }

    @Test
    void withdrawalBeyondBalanceRespondsWithConflict() {
        Account account = repository.save(new Account(null, "Checking", 100L));

        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
            httpClient.toBlocking().exchange(
                HttpRequest.POST("/accounts/" + account.id() + "/withdraw?amount=1000", null)));

        assertEquals(HttpStatus.CONFLICT, e.getStatus()); // <5>
        assertTrue(e.getResponse().getBody(String.class).orElse("")
            .contains("The operation violates an account constraint")); // <6>
    }

    @Test
    void accountIsCreatedDepositedAndWithdrawnOverHttp() {
        BlockingHttpClient client = httpClient.toBlocking();

        HttpResponse<Account> created = client.exchange(
            HttpRequest.POST("/accounts", new Account(null, "Savings", 100L)), Account.class); // <7>
        assertEquals(HttpStatus.CREATED, created.getStatus());
        Account account = created.body();

        Account deposited = client.retrieve(
            HttpRequest.POST("/accounts/" + account.id() + "/deposit?amount=25", null), Account.class); // <8>
        assertEquals(125L, deposited.balance());

        Account withdrawn = client.retrieve(
            HttpRequest.POST("/accounts/" + account.id() + "/withdraw?amount=50", null), Account.class);
        assertEquals(75L, withdrawn.balance());
    }

    @Test
    void depositForUnknownAccountRespondsWithNotFound() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
            httpClient.toBlocking().exchange(
                HttpRequest.POST("/accounts/-1/deposit?amount=1", null)));

        assertEquals(HttpStatus.NOT_FOUND, e.getStatus()); // <9>
    }
}
