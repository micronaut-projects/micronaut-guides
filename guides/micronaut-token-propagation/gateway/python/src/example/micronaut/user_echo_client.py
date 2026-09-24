from abc import abstractmethod

from micronaut.context.annotation import Requires
from micronaut.context.env import Environment
from micronaut.http import MediaType
from micronaut.http.annotation import Consumes, Get
from micronaut.http.client.annotation import Client
from org.reactivestreams import Publisher

from .username_fetcher import UsernameFetcher


# tag::clazz[]
@Client(id="userecho")
@Requires(notEnv=Environment.TEST)
class UserEchoClient(UsernameFetcher):

    @Consumes(MediaType.TEXT_PLAIN)
    @Get("/user")
    @abstractmethod
    def findUsername(self) -> Publisher[str]:
        ...
# end::clazz[]
