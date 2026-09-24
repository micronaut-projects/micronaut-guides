from graphql.schema import DataFetcher, DataFetchingEnvironment
from jakarta.inject import Singleton

from .to_do import ToDo
from .to_do_repository import ToDoRepository


@Singleton  # <1>
class CompleteToDoDataFetcher(DataFetcher[bool]):
    def __init__(self, to_do_repository: ToDoRepository):  # <2>
        self._to_do_repository = to_do_repository

    def get(self, environment: DataFetchingEnvironment) -> bool:
        todo_id = int(environment.getArgument("id"))
        to_do = self._to_do_repository.findById(todo_id).orElse(None)  # <3>
        if to_do is None:
            return False
        return self._set_completed_and_update(to_do)

    def _set_completed_and_update(self, to_do: ToDo) -> bool:
        to_do.completed = True  # <4>
        self._to_do_repository.update(to_do)  # <5>
        return True
