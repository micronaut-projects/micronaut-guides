from micronaut.security.authentication import Authentication
from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule


# tag::clazz[]
@Produces(MediaType.TEXT_PLAIN)
@Secured(SecurityRule.IS_AUTHENTICATED)  # <1>
@Get("/")  # <2> <3>
def index(authentication: Authentication) -> str:  # <4>
    return authentication.getName()
# end::clazz[]
