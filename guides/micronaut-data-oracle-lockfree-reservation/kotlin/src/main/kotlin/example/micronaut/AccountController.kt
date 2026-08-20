package example.micronaut

import example.micronaut.domain.Account
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue

@Controller("/accounts")
class AccountController(private val repository: AccountRepository) {

    @Post(consumes = [MediaType.APPLICATION_JSON])
    fun create(@Body account: Account): Account = repository.save(account)

    @Get(produces = [MediaType.APPLICATION_JSON])
    fun findAll(): List<Account> = repository.findAll()

    @Post("/{id}/reserve")
    fun reserve(@PathVariable id: Long, @QueryValue balance: Long, @QueryValue credit: Long): Account {
        repository.reserveIncrementBalanceAndDecrementCredit(id, balance, credit)
        return repository.findById(id).orElseThrow()
    }
}
