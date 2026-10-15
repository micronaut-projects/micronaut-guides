from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule

@Produces(MediaType.TEXT_PLAIN)  # <2>
@Secured(SecurityRule.IS_AUTHENTICATED)  # <3>
@Get("/app")  # <1> <4>
def index() -> str:
    return "Top Secret"
