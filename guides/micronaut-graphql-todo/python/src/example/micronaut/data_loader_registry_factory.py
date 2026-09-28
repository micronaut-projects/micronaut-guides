import logging

from micronaut.context.annotation import Factory
from micronaut.runtime.http.scope import RequestScope
from org.dataloader import DataLoaderFactory, DataLoaderRegistry

from .author_data_loader import AuthorDataLoader

LOG = logging.getLogger(__name__)


@Factory  # <1>
class DataLoaderRegistryFactory:
    @RequestScope  # <2>
    def data_loader_registry(
        self,
        author_data_loader: AuthorDataLoader,
    ) -> DataLoaderRegistry:
        data_loader = DataLoaderFactory.newMappedDataLoader(author_data_loader)
        data_loader_registry = DataLoaderRegistry()
        data_loader_registry.register("author", data_loader)  # <3>

        LOG.debug("Created new data loader registry")
        return data_loader_registry
