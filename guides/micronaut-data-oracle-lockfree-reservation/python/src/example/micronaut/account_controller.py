from typing import Annotated

from micronaut.http import HttpResponse, HttpStatus
from micronaut.http.annotation import Body, Controller, Get, Post, QueryValue, Status
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from .account_repository import AccountRepository
from .domain.account import Account


@ExecuteOn(TaskExecutors.BLOCKING)  # <1>
@Controller("/accounts")  # <2>
class AccountController:
    def __init__(self, repository: AccountRepository):  # <3>
        self.repository = repository

    @Post
    @Status(HttpStatus.CREATED)
    def create(self, account: Annotated[Account, Body]) -> Account:
        return self.repository.save(account)

    @Get
    def find_all(self) -> list[Account]:
        return self.repository.findAll()

    @Post("/{id}/deposit")
    def deposit(self, id: int, amount: Annotated[int, QueryValue]) -> HttpResponse[Account]:
        if self.repository.reserveIncrementBalance(id, amount) == 0:  # <4>
            return HttpResponse.notFound()
        return HttpResponse.ok(self.repository.findById(id).orElseThrow())  # <5>

    @Post("/{id}/withdraw")
    def withdraw(self, id: int, amount: Annotated[int, QueryValue]) -> HttpResponse[Account]:
        if self.repository.reserveDecrementBalance(id, amount) == 0:
            return HttpResponse.notFound()
        return HttpResponse.ok(self.repository.findById(id).orElseThrow())
