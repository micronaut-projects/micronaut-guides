from typing import Annotated

from jakarta.inject import Inject
from micronaut.http.annotation import Body, Post
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn

from .assistant import Assistant

assistant: Annotated[Assistant, Inject]  # <1>


@ExecuteOn(TaskExecutors.BLOCKING)  # <2>
@Post(value="/assistant", consumes="text/plain", produces="text/plain")  # <3>
def chat(message: Annotated[str, Body]) -> str:
    return assistant.chat(message)
