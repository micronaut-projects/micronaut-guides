from java.security import Principal
from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule


@Secured(SecurityRule.IS_AUTHENTICATED)
@Produces(MediaType.TEXT_PLAIN)
@Get("/user")
def index(principal: Principal) -> str:
    return principal.getName()
