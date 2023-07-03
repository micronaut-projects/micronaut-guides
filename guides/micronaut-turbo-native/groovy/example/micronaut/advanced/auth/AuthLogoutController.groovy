package example.micronaut.advanced.auth

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.event.ApplicationEvent
import io.micronaut.context.event.ApplicationEventPublisher
import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.endpoints.LogoutController
import io.micronaut.security.event.LogoutEvent
import io.micronaut.security.handlers.LogoutHandler

import java.security.Principal

@Replaces(LogoutController.class)
@Controller("/signout")
class AuthLogoutController {

    private final LogoutHandler<HttpRequest<?>, MutableHttpResponse<?>> logoutHandler
    private final ApplicationEventPublisher<ApplicationEvent> eventPublisher

    AuthLogoutController(LogoutHandler<HttpRequest<?>, MutableHttpResponse<?>> logoutHandler, ApplicationEventPublisher<ApplicationEvent> eventPublisher) {
        this.logoutHandler = logoutHandler
        this.eventPublisher = eventPublisher
    }

    @Post(consumes = [MediaType.TEXT_HTML, MediaType.APPLICATION_FORM_URLENCODED], produces = [MediaType.TEXT_HTML])
    MutableHttpResponse<?> logout(HttpRequest<?> request, @Nullable Principal principal) {
        if (principal) {
            eventPublisher.publishEvent(new LogoutEvent(principal))
        }
        logoutHandler.logout(request)
    }
}