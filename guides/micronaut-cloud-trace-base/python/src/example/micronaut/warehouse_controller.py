import random
from time import sleep

from micronaut.http import HttpResponse
from micronaut.http.annotation import Controller, Get, Post
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn


@ExecuteOn(TaskExecutors.BLOCKING)  # <1>
@Controller("/warehouse")  # <2>
class WarehouseController:

    @Get("/count")  # <3>
    def get_item_count(self) -> HttpResponse:
        return HttpResponse.ok(random.randint(0, 10))

    @Post("/order")  # <4>
    def order(self) -> HttpResponse:
        sleep(0.5)
        return HttpResponse.accepted()
