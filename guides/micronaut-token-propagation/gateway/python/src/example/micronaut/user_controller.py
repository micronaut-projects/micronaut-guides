from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule

from .username_fetcher import UsernameFetcher


# tag::clazz[]
@Controller("/user")
class UserController:
    def __init__(self, username_fetcher: UsernameFetcher):
        self.username_fetcher = username_fetcher

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Produces(MediaType.TEXT_PLAIN)
    @Get
    async def index(self) -> str:
        return await self.username_fetcher.find_username()
# end::clazz[]
