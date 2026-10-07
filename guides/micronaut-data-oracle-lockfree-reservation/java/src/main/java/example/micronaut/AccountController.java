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
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.annotation.Status;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import java.util.List;

@ExecuteOn(TaskExecutors.BLOCKING) // <1>
@Controller("/accounts") // <2>
public class AccountController {

    private final AccountRepository repository;

    AccountController(AccountRepository repository) { // <3>
        this.repository = repository;
    }

    @Post
    @Status(HttpStatus.CREATED)
    public Account create(@Body Account account) {
        return repository.save(account);
    }

    @Get
    public List<Account> findAll() {
        return repository.findAll();
    }

    @Post("/{id}/deposit")
    public Account deposit(Long id, @QueryValue long amount) {
        if (repository.reserveIncrementBalance(id, amount) == 0) { // <4>
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        return repository.findById(id).orElseThrow(); // <5>
    }

    @Post("/{id}/withdraw")
    public Account withdraw(Long id, @QueryValue long amount) {
        if (repository.reserveDecrementBalance(id, amount) == 0) {
            throw new HttpStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        return repository.findById(id).orElseThrow();
    }
}
