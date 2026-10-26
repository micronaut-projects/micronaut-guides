from typing import Annotated

from java.net import URI
from micronaut.http import HttpResponse, MediaType
from micronaut.http.annotation import Body, Consumes, Controller, Post
from micronaut.http.cookie import Cookie
from micronaut.security.annotation import Secured
from micronaut.security.rules import SecurityRule
from jakarta.validation.constraints import NotBlank


@Controller("/locale")  # <1>
class LocaleController:

    @Secured(SecurityRule.IS_ANONYMOUS)  # <2>
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)  # <3>
    @Post  # <4>
    def change_locale(self, locale: Annotated[str, Body("locale"), NotBlank]) -> HttpResponse:
        return HttpResponse.seeOther(URI("/")).cookie(Cookie.of("locale", locale))  # <5>
