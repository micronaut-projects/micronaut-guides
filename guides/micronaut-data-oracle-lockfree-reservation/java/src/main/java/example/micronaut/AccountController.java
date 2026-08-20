package example.micronaut;

import example.micronaut.domain.Account;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;

import java.util.List;

@Controller("/accounts")
public class AccountController {

    private final AccountRepository repository;

    public AccountController(AccountRepository repository) {
        this.repository = repository;
    }

    @Post(consumes = MediaType.APPLICATION_JSON)
    public Account create(@Body Account account) {
        return repository.save(account);
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public List<Account> findAll() {
        return repository.findAll();
    }

    @Post("/{id}/reserve")
    public Account reserve(@PathVariable Long id, @QueryValue Long balance, @QueryValue Long credit) {
        repository.reserveIncrementBalanceAndDecrementCredit(id, balance, credit);
        return repository.findById(id).orElseThrow();
    }
}
