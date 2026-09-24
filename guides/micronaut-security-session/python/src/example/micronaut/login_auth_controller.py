from micronaut.http.annotation import Controller, Get
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule
from micronaut.views import View


# tag::clazz[]
@Secured(SecurityRule.IS_ANONYMOUS)  # <1>
@Controller("/login")  # <2>
class LoginAuthController:

    @Get("/auth")  # <3>
    @View("auth")  # <4>
    def auth(self) -> dict:
        return {}

    @Get("/authFailed")  # <5>
    @View("auth")  # <4>
    def auth_failed(self) -> dict:
        return {"errors": True}
# end::clazz[]
