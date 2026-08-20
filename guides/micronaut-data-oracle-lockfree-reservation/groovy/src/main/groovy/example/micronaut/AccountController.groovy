package example.micronaut

import example.micronaut.domain.Account
import groovy.transform.CompileStatic
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue

@CompileStatic
@Controller('/accounts')
class AccountController {

    private final AccountRepository repository

    AccountController(AccountRepository repository) {
        this.repository = repository
    }

    @Post(consumes = MediaType.APPLICATION_JSON)
    Account create(@Body Account account) {
        repository.save(account)
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    List<Account> findAll() {
        repository.findAll()
    }

    @Post('/{id}/reserve')
    Account reserve(@PathVariable Long id, @QueryValue Long balance, @QueryValue Long credit) {
        repository.reserveIncrementBalanceAndDecrementCredit(id, balance, credit)
        repository.findById(id).orElseThrow()
    }
}
