from graphql.schema import DataFetcher, DataFetchingEnvironment
from jakarta.inject import Singleton

from .to_do import ToDo
from .to_do_repository import ToDoRepository


@Singleton  # <1>
class ToDosDataFetcher(DataFetcher[list[ToDo]]):
    def __init__(self, to_do_repository: ToDoRepository):  # <2>
        self._to_do_repository = to_do_repository

    def get(self, environment: DataFetchingEnvironment) -> object:
        return self._to_do_repository.findAll()
