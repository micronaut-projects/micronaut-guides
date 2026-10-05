from micronaut.http.annotation import Controller, Get
from micronaut.security.annotation import Secured
from micronaut.security.authentication import Authentication
from micronaut.security.rules import SecurityRule
from micronaut.views import View


# tag::clazz[]
@Secured(SecurityRule.IS_ANONYMOUS)  # <1>
@Controller  # <2>
class HomeController:

    @Get("/")  # <3>
    @View("home")  # <4>
    def index(self, authentication: Authentication | None = None) -> dict:  # <5>
        model = {"loggedIn": authentication is not None}
        if authentication is not None:
            model["username"] = authentication.getName()
        return model
# end::clazz[]
