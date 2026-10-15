from micronaut.http.annotation import Get
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule
from micronaut.views import View


@Secured(SecurityRule.IS_ANONYMOUS)  # <1>
@View("home")  # <2>
@Get("/")  # <3>
def index() -> dict:
    return {}


@Secured(SecurityRule.IS_AUTHENTICATED)  # <4>
@Get("/secure")  # <5>
def secured() -> dict:
    return {"secured": True}  # <6>
