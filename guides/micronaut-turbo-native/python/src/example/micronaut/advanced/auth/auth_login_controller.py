from typing import Annotated

from micronaut.context.annotation import Replaces
from micronaut.context.event import ApplicationEventPublisher
from micronaut.core.async_.annotation import SingleResult
from micronaut.http import HttpRequest, HttpResponse, HttpStatus, MediaType, MutableHttpResponse
from micronaut.http.annotation import Controller, Part, Post
from micronaut.http.server.util import HttpHostResolver
from micronaut.http.server.util.locale import HttpLocaleResolver
from micronaut.security.authentication import AuthenticationRequest, Authenticator
from micronaut.security.endpoints import LoginController
from micronaut.security.event import LoginFailedEvent, LoginSuccessfulEvent
from micronaut.security.handlers import LoginHandler
from org.reactivestreams import Publisher
from reactor.core.publisher import Flux


class NameAuthenticationRequest(AuthenticationRequest):
    def __init__(self, name: str | None):
        self.name = name

    def getIdentity(self) -> str | None:
        return self.name

    def getSecret(self) -> str:
        return ""


@Replaces(LoginController)
@Controller("/login")
class AuthLoginController:
    def __init__(
        self,
        http_host_resolver: HttpHostResolver,
        http_locale_resolver: HttpLocaleResolver,
        authenticator: Authenticator,
        login_handler: LoginHandler,
        event_publisher: ApplicationEventPublisher,
    ):
        self.http_host_resolver = http_host_resolver
        self.http_locale_resolver = http_locale_resolver
        self.authenticator = authenticator
        self.login_handler = login_handler
        self.event_publisher = event_publisher

    @SingleResult
    @Post(
        consumes=[MediaType.TEXT_HTML, MediaType.MULTIPART_FORM_DATA],
        produces=MediaType.TEXT_HTML,
    )
    def login(
        self,
        name: Annotated[str | None, Part] = None,
        request: HttpRequest = None,
    ) -> Publisher[MutableHttpResponse]:
        auth = NameAuthenticationRequest(name)
        host = self.http_host_resolver.resolve(request)
        locale = self.http_locale_resolver.resolveOrDefault(request)

        def handle(authentication_response):
            if (
                authentication_response.isAuthenticated()
                and authentication_response.getAuthentication().isPresent()
            ):
                authentication = authentication_response.getAuthentication().get()
                self.event_publisher.publishEvent(
                    LoginSuccessfulEvent(auth, host, locale)
                )
                return self.login_handler.loginSuccess(authentication, request)

            self.event_publisher.publishEvent(
                LoginFailedEvent(authentication_response, auth, host, locale)
            )
            return self.login_handler.loginFailed(authentication_response, request)

        return (
            Flux.from_(self.authenticator.authenticate(request, auth))
            .map(handle)
            .defaultIfEmpty(HttpResponse.status(HttpStatus.UNAUTHORIZED))
        )
