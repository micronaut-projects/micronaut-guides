from java.security import Principal
from micronaut.context.annotation import Replaces
from micronaut.context.event import ApplicationEventPublisher
from micronaut.http import HttpRequest, MediaType
from micronaut.http.annotation import Controller, Post
from micronaut.http.server.util import HttpHostResolver
from micronaut.http.server.util.locale import HttpLocaleResolver
from micronaut.security.endpoints import LogoutController
from micronaut.security.event import LogoutEvent
from micronaut.security.handlers import LogoutHandler


@Replaces(LogoutController)
@Controller("/signout")
class AuthLogoutController:
    def __init__(
        self,
        http_host_resolver: HttpHostResolver,
        http_locale_resolver: HttpLocaleResolver,
        logout_handler: LogoutHandler,
        event_publisher: ApplicationEventPublisher,
    ):
        self.http_host_resolver = http_host_resolver
        self.http_locale_resolver = http_locale_resolver
        self.logout_handler = logout_handler
        self.event_publisher = event_publisher

    @Post(
        consumes=[MediaType.TEXT_HTML, MediaType.APPLICATION_FORM_URLENCODED],
        produces=MediaType.TEXT_HTML,
    )
    def logout(self, request: HttpRequest, principal: Principal | None = None):
        if principal is not None:
            host = self.http_host_resolver.resolve(request)
            locale = self.http_locale_resolver.resolveOrDefault(request)
            self.event_publisher.publishEvent(LogoutEvent(principal, host, locale))
        return self.logout_handler.logout(request)
