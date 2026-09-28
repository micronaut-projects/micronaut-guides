from java.security import Principal
from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule


# tag::clazz[]
@Secured(SecurityRule.IS_AUTHENTICATED)  # <2>
@Produces(MediaType.TEXT_PLAIN)  # <3>
@Get("/user")  # <1> <4>
def index(principal: Principal) -> str:  # <5>
    return principal.getName()
# end::clazz[]
