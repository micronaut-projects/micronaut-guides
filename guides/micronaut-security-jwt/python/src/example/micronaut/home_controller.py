from java.security import Principal
from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule


# tag::clazz[]
@Produces(MediaType.TEXT_PLAIN)
@Secured(SecurityRule.IS_AUTHENTICATED)  # <1>
@Get("/")  # <2> <3>
def index(principal: Principal) -> str:  # <4>
    return principal.getName()
# end::clazz[]
