from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.authentication import Authentication
from micronaut.security.rules import SecurityRule


# tag::clazz[]
@Secured(SecurityRule.IS_AUTHENTICATED)  # <2>
@Produces(MediaType.TEXT_PLAIN)  # <3>
@Get("/user")  # <1> <4>
def index(authentication: Authentication) -> str:  # <5>
    return authentication.getName()
# end::clazz[]
