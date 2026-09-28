from abc import ABC, abstractmethod
from typing import Annotated

from micronaut.http import MediaType
from micronaut.http.annotation import Consumes, Get, Header
from micronaut.http.client.annotation import Client


# tag::clazz[]
@Client("/")
class AppClient(ABC):
    @Consumes(MediaType.TEXT_PLAIN)  # <1>
    @Get("/")
    @abstractmethod
    def home(self, authorization: Annotated[str, Header]) -> str:  # <2>
        ...
# end::clazz[]
