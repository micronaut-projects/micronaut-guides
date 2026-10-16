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
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(transactional = false) // <1>
class AccountSpec extends Specification {

    @Inject
    AccountRepository repository

    @Inject
    @Client('/')
    HttpClient httpClient

    void 'deposit increments balance'() {
        given:
        Account account = repository.save(new Account(null, 'Checking', 100L))

        when:
        long updated = repository.reserveIncrementBalance(account.id, 25L) // <2>

        then:
        updated == 1L
        Account found = repository.findById(account.id).orElseThrow()
        found.balance == 125L
    }

    void 'withdrawal beyond balance is rejected'() {
        given:
        Account account = repository.save(new Account(null, 'Checking', 100L))

        when:
        repository.reserveDecrementBalance(account.id, 1000L) // <3>

        then:
        thrown(DataIntegrityViolationException)
        Account found = repository.findById(account.id).orElseThrow()
        found.balance == 100L // <4>
    }

    void 'withdrawal beyond balance responds with conflict'() {
        given:
        Account account = repository.save(new Account(null, 'Checking', 100L))

        when:
        httpClient.toBlocking().exchange(
                HttpRequest.POST("/accounts/${account.id}/withdraw?amount=1000", null))

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.CONFLICT // <5>
        e.response.getBody(String).orElse('').contains('The operation violates an account constraint') // <6>
    }

    void 'account is created, deposited and withdrawn over HTTP'() {
        when:
        HttpResponse<Account> created = httpClient.toBlocking().exchange(
                HttpRequest.POST('/accounts', new Account(null, 'Savings', 100L)), Account) // <7>

        then:
        created.status == HttpStatus.CREATED

        when:
        Account deposited = httpClient.toBlocking().retrieve(
                HttpRequest.POST("/accounts/${created.body().id}/deposit?amount=25", null), Account) // <8>

        then:
        deposited.balance == 125L

        when:
        Account withdrawn = httpClient.toBlocking().retrieve(
                HttpRequest.POST("/accounts/${created.body().id}/withdraw?amount=50", null), Account)

        then:
        withdrawn.balance == 75L
    }

    void 'deposit for unknown account responds with not found'() {
        when:
        httpClient.toBlocking().exchange(HttpRequest.POST('/accounts/-1/deposit?amount=1', null))

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.NOT_FOUND // <9>
    }
}
