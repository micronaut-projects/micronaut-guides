from typing import Annotated

from micronaut.http import HttpHeaders, MediaType
from micronaut.http.annotation import Controller, Get, Header, Produces
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule
from org.reactivestreams import Publisher

from .username_fetcher import UsernameFetcher


# tag::clazz[]
@Controller("/user")  # <1>
class UserController:
    def __init__(self, username_fetcher: UsernameFetcher):  # <2>
        self.username_fetcher = username_fetcher

    @Secured(SecurityRule.IS_AUTHENTICATED)  # <3>
    @Produces(MediaType.TEXT_PLAIN)  # <4>
    @Get  # <5>
    def index(
        self,
        authorization: Annotated[str, Header(HttpHeaders.AUTHORIZATION)],  # <6>
    ) -> Publisher[str]:
        return self.username_fetcher.findUsername(authorization)
# end::clazz[]
