from abc import ABC, abstractmethod
from typing import Annotated

from micronaut.http import HttpHeaders, MediaType
from micronaut.http.annotation import Body, Consumes, Get, Header, Post
from micronaut.http.client.annotation import Client
from micronaut.security.authentication import UsernamePasswordCredentials
from micronaut.security.token.render import BearerAccessRefreshToken


# tag::clazz[]
@Client("/")
class AppClient(ABC):
    @Post("/login")
    @abstractmethod
    def login(
        self,
        credentials: Annotated[UsernamePasswordCredentials, Body],
    ) -> BearerAccessRefreshToken:
        ...

    @Consumes(MediaType.TEXT_PLAIN)
    @Get("/")
    @abstractmethod
    def home(self, authorization: Annotated[str, Header(HttpHeaders.AUTHORIZATION)]) -> str:
        ...
# end::clazz[]
