from java.security import Principal
from micronaut.http.annotation import Controller, Get
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule
from micronaut.views import View


# tag::clazz[]
@Secured(SecurityRule.IS_ANONYMOUS)  # <1>
@Controller  # <2>
class HomeController:

    @Get("/")  # <3>
    @View("home")  # <4>
    def index(self, principal: Principal | None = None) -> dict:  # <5>
        model = {"loggedIn": principal is not None}
        if principal is not None:
            model["username"] = principal.getName()
        return model
# end::clazz[]
