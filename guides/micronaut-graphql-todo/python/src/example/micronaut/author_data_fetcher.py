from graphql.schema import DataFetcher, DataFetchingEnvironment
from jakarta.inject import Singleton
from java.util.concurrent import CompletionStage
from org.dataloader import DataLoader

from .to_do import ToDo


@Singleton  # <1>
class AuthorDataFetcher(DataFetcher[CompletionStage]):
    def get(self, environment: DataFetchingEnvironment) -> CompletionStage:
        to_do: ToDo = environment.getSource()
        author_data_loader: DataLoader = environment.getDataLoader("author")  # <2>
        return author_data_loader.load(to_do.authorId)
