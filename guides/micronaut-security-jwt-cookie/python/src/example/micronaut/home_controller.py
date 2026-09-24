from micronaut.http import HttpRequest
from micronaut.http.annotation import Controller, Get
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule
from micronaut.views import View


# tag::clazz[]
@Secured(SecurityRule.IS_ANONYMOUS)  # <1>
@Controller  # <2>
class HomeController:
    @Get  # <3>
    @View("home")  # <4>
    def index(self, request: HttpRequest) -> dict:  # <5>
        principal = request.getUserPrincipal().orElse(None)
        data = {"loggedIn": principal is not None}
        if principal is not None:
            data["username"] = principal.getName()
        return data
# end::clazz[]
