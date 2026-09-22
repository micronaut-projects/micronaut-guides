from micronaut.http import MediaType
from micronaut.http.annotation import Controller, Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.authentication import Authentication
from micronaut.security.rules import SecurityRule


# tag::clazz[]
@Controller  # <1>
class PlanController:

    @Produces(MediaType.TEXT_PLAIN)  # <2>
    @Secured(SecurityRule.IS_AUTHENTICATED)  # <3>
    @Get("/plan")  # <4>
    def index(self, authentication: Authentication) -> str:  # <5>
        if "ROLE_EVIL_MASTERMIND" in authentication.getRoles():
            return "Kill Sherlock Holmes and his companions"
        return "Plan New Year"
# end::clazz[]
