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
    void reservationUpdatesMultipleFields() {
        Account account = repository.save(new Account(null, "Checking", 100L, 50L));

        long updated = repository.reserveIncrementBalanceAndDecrementCredit(account.id(), 25L, 25L); // <2>

        assertEquals(1L, updated);
        Account found = repository.findById(account.id()).orElseThrow();
        assertEquals(125L, found.balance());
        assertEquals(25L, found.credit());
    }

    @Test
    void reservationConstraintFailureIsMapped() {
        Account account = repository.save(new Account(null, "Checking", 100L, 50L));

        assertThrows(DataIntegrityViolationException.class,
            () -> repository.reserveIncrementBalanceAndDecrementCredit(account.id(), 1000L, 1000L)); // <3>

        Account found = repository.findById(account.id()).orElseThrow();
        assertEquals(100L, found.balance()); // <4>
        assertEquals(50L, found.credit());
    }

    @Test
    void reservationConstraintFailureRespondsWithConflict() {
        Account account = repository.save(new Account(null, "Checking", 100L, 50L));

        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
            httpClient.toBlocking().exchange(
                HttpRequest.POST("/accounts/" + account.id() + "/reserve?balance=1000&credit=1000", null)));

        assertEquals(HttpStatus.CONFLICT, e.getStatus()); // <5>
        assertTrue(e.getResponse().getBody(String.class).orElse("")
            .contains("The operation violates an account constraint")); // <6>
    }

    @Test
    void accountIsCreatedAndReservedOverHttp() {
        BlockingHttpClient client = httpClient.toBlocking();

        HttpResponse<Account> created = client.exchange(
            HttpRequest.POST("/accounts", new Account(null, "Savings", 100L, 50L)), Account.class); // <7>
        assertEquals(HttpStatus.CREATED, created.getStatus());
        Account account = created.body();

        Account reserved = client.retrieve(
            HttpRequest.POST("/accounts/" + account.id() + "/reserve?balance=25&credit=25", null), Account.class); // <8>
        assertEquals(125L, reserved.balance());
        assertEquals(25L, reserved.credit());
    }

    @Test
    void reservationForUnknownAccountRespondsWithNotFound() {
        HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () ->
            httpClient.toBlocking().exchange(
                HttpRequest.POST("/accounts/-1/reserve?balance=1&credit=1", null)));

        assertEquals(HttpStatus.NOT_FOUND, e.getStatus()); // <9>
    }
}
