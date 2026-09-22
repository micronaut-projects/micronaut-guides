from java.security import Principal
from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule


# tag::clazz[]
@Produces(MediaType.TEXT_PLAIN)  # <2>
@Secured(SecurityRule.IS_AUTHENTICATED)  # <4>
@Get("/api")  # <1> <3>
def index(principal: Principal) -> str:  # <5>
    return f"Hello {principal.getName()}"
# end::clazz[]
