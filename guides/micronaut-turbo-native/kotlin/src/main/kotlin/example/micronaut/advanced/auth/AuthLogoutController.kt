package example.micronaut.advanced.auth

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.event.ApplicationEvent
import io.micronaut.context.event.ApplicationEventPublisher
import io.micronaut.http.HttpRequest
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.endpoints.LogoutController
import io.micronaut.security.event.LogoutEvent
import io.micronaut.security.handlers.LogoutHandler
import java.security.Principal

@Replaces(LogoutController::class)
@Controller("/signout")
class AuthLogoutController(
    val logoutHandler: LogoutHandler<HttpRequest<*>, MutableHttpResponse<*>>,
    val eventPublisher: ApplicationEventPublisher<ApplicationEvent>
) {

    @Post(consumes = [MediaType.TEXT_HTML, MediaType.APPLICATION_FORM_URLENCODED], produces = [MediaType.TEXT_HTML])
    fun logout(request: HttpRequest<*>, principal: Principal?): MutableHttpResponse<*> {
        if (principal != null) {
            eventPublisher.publishEvent(LogoutEvent(principal))
        }
        return logoutHandler.logout(request)
    }
}