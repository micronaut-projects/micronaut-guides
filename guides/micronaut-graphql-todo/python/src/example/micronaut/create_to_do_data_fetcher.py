from graphql.schema import DataFetcher, DataFetchingEnvironment
from jakarta.inject import Singleton
from jakarta.transaction import Transactional

from .author_repository import AuthorRepository
from .to_do import ToDo
from .to_do_repository import ToDoRepository


@Singleton  # <1>
class CreateToDoDataFetcher(DataFetcher[ToDo]):
    def __init__(
        self,
        to_do_repository: ToDoRepository,  # <2>
        author_repository: AuthorRepository,
    ):
        self._to_do_repository = to_do_repository
        self._author_repository = author_repository

    @Transactional
    def get(self, environment: DataFetchingEnvironment) -> ToDo:
        title = environment.getArgument("title")
        username = environment.getArgument("author")

        author = self._author_repository.findOrCreate(username)  # <3>
        return self._to_do_repository.save(ToDo(title, author.id))  # <4>
