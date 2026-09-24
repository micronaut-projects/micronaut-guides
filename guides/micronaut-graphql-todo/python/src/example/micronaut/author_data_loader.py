from typing import Annotated

from jakarta.inject import Named, Singleton
from java.util import HashMap
from java.util.concurrent import CompletableFuture, ExecutorService
from micronaut.scheduling import TaskExecutors
from org.dataloader import MappedBatchLoader

from .author import Author
from .author_repository import AuthorRepository


@Singleton  # <1>
class AuthorDataLoader(MappedBatchLoader[int, Author]):
    def __init__(
        self,
        author_repository: AuthorRepository,
        executor: Annotated[ExecutorService, Named(TaskExecutors.BLOCKING)],  # <2>
    ):
        self._author_repository = author_repository
        self._executor = executor

    def load(self, keys) -> object:
        def find_authors():
            authors = HashMap()
            for author in self._author_repository.findByIdIn(keys):
                authors.put(author.id, author)
            return authors

        return CompletableFuture.supplyAsync(find_authors, self._executor)
