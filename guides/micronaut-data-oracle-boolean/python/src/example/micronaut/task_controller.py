from typing import Annotated

from micronaut.http import HttpResponse, HttpStatus
from micronaut.http.annotation import Body, Controller, Get, Post, Put, Status
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from .task import Task
from .task_repository import TaskRepository


@ExecuteOn(TaskExecutors.BLOCKING)  # <1>
@Controller("/tasks")
class TaskController:
    def __init__(self, task_repository: TaskRepository):
        self.task_repository = task_repository

    @Post
    @Status(HttpStatus.CREATED)
    def create(self, title: Annotated[str, Body("title")]) -> Task:  # <2>
        return self.task_repository.save(Task(title))

    @Get("/open")
    def open(self) -> list[Task]:
        return self.task_repository.findByCompletedFalse()

    @Get("/completed")
    def completed(self) -> list[Task]:
        return self.task_repository.findByCompletedTrue()

    @Put("/{id}/complete")
    def complete(self, id: int) -> HttpResponse[Task]:
        if self.task_repository.updateCompleted(id, True) == 0:  # <3>
            return HttpResponse.notFound()
        return HttpResponse.ok(self.task_repository.findById(id).orElseThrow())
