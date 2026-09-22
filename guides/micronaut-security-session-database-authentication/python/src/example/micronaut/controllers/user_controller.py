from typing import Annotated

from micronaut.http import HttpRequest, HttpResponse, MediaType
from micronaut.http.annotation import Body, Consumes, Controller, Get, Post, Produces
from micronaut.http.uri import UriBuilder
from micronaut.scheduling import TaskExecutors
from micronaut.scheduling.annotation import ExecuteOn
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule
from micronaut.views import ModelAndView, View

from ..exceptions.user_already_exists_exception import UserAlreadyExistsException
from ..register_service import RegisterService

MESSAGE_LOGIN_FAILED = "That username or password is incorrect. Please try again."
MESSAGE_SIGNUP_FAILED = "Sorry, someone already has that username."


# tag::clazz[]
@Secured(SecurityRule.IS_ANONYMOUS)  # <1>
@Controller("/user")  # <2>
class UserController:
    def __init__(self, register_service: RegisterService):  # <3>
        self.register_service = register_service
        self.uri_auth = UriBuilder.of("/user").path("auth").build()

    @Produces(MediaType.TEXT_HTML)  # <4>
    @Get("/auth")  # <5>
    @View("/user/auth.html")  # <6>
    def auth(self) -> dict:
        return {}

    @Produces(MediaType.TEXT_HTML)
    @Get("/authFailed")  # <7>
    @View("/user/auth.html")
    def auth_failed(self) -> dict:
        return {"error": MESSAGE_LOGIN_FAILED}

    @ExecuteOn(TaskExecutors.BLOCKING)  # <8>
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)  # <9>
    @Produces(MediaType.TEXT_HTML)
    @Post("/signup")  # <10>
    def sign_up_save(
        self,
        request: HttpRequest,  # <11>
        username: Annotated[str, Body("username")],
        password: Annotated[str, Body("password")],
        repeatPassword: Annotated[str, Body("repeatPassword")],
    ) -> HttpResponse:
        if not username or not password or not repeatPassword or password != repeatPassword:
            return signup_error(request, username, "Passwords do not match")
        try:
            self.register_service.register(username, password, [])
        except UserAlreadyExistsException:
            return signup_error(request, username, MESSAGE_SIGNUP_FAILED)
        return HttpResponse.seeOther(self.uri_auth)

    @Produces(MediaType.TEXT_HTML)
    @Get("/signup")  # <12>
    @View("/user/signup.html")
    def sign_up(self) -> dict:
        return {}


def signup_error(request: HttpRequest, username: str, error: str) -> HttpResponse:  # <13>
    return HttpResponse.unprocessableEntity().body(
        ModelAndView("/user/signup.html", {"username": username, "error": error})
    )
# end::clazz[]
