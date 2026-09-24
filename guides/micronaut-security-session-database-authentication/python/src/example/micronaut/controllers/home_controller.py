from micronaut.http import MediaType
from micronaut.http.annotation import Get, Produces
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule
from micronaut.views import View


# tag::clazz[]
@Produces(MediaType.TEXT_HTML)  # <1>
@Secured(SecurityRule.IS_ANONYMOUS)  # <2>
@Get("/")  # <3>
@View("home.html")  # <4>
def index() -> dict:  # <5>
    return {}
# end::clazz[]
