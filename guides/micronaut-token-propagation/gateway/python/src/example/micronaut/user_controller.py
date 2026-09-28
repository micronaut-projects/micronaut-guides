from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule
from org.reactivestreams import Publisher

from .username_fetcher import UsernameFetcher


# tag::clazz[]
@Controller("/user")
class UserController:
    def __init__(self, username_fetcher: UsernameFetcher):
        self.username_fetcher = username_fetcher

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Produces(MediaType.TEXT_PLAIN)
    @Get
    def index(self) -> Publisher[str]:
        return self.username_fetcher.findUsername()
# end::clazz[]
