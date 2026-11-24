from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule


@Controller("/account")  # <1>
class AccountController:

    @Secured(SecurityRule.IS_AUTHENTICATED)  # <2>
    @Produces(MediaType.TEXT_PLAIN)  # <3>
    @Get  # <4>
    def index(self) -> str:
        return "Your account"
