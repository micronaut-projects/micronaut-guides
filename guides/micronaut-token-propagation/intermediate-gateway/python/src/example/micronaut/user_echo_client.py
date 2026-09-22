from abc import abstractmethod
from typing import Annotated

from micronaut.context.annotation import Requires
from micronaut.context.env import Environment
from micronaut.http import HttpHeaders, MediaType
from micronaut.http.annotation import Consumes, Get, Header
from micronaut.http.client.annotation import Client
from org.reactivestreams import Publisher

from .username_fetcher import UsernameFetcher


# tag::clazz[]
@Client(id="userecho")  # <1>
@Requires(notEnv=Environment.TEST)  # <2>
class UserEchoClient(UsernameFetcher):

    @Consumes(MediaType.TEXT_PLAIN)
    @Get("/user")  # <3>
    @abstractmethod
    def findUsername(
        self,
        authorization: Annotated[str, Header(HttpHeaders.AUTHORIZATION)],  # <4>
    ) -> Publisher[str]:
        ...
# end::clazz[]
