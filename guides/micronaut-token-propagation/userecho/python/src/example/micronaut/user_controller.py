from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.authentication import Authentication
from micronaut.security.rules import SecurityRule


@Secured(SecurityRule.IS_AUTHENTICATED)
@Produces(MediaType.TEXT_PLAIN)
@Get("/user")
def index(authentication: Authentication) -> str:
    return authentication.getName()
