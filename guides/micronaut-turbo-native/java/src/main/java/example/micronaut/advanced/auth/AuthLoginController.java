package example.micronaut.advanced.auth;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.event.ApplicationEvent;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Part;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.AuthenticationRequest;
import io.micronaut.security.authentication.Authenticator;
import io.micronaut.security.endpoints.LoginController;
import io.micronaut.security.event.LoginFailedEvent;
import io.micronaut.security.event.LoginSuccessfulEvent;
import io.micronaut.security.handlers.LoginHandler;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;

@Replaces(LoginController.class)
@Controller("/login")
class AuthLoginController {

    private final Authenticator authenticator;
    private final LoginHandler loginHandler;
    private final ApplicationEventPublisher<ApplicationEvent> eventPublisher;

    AuthLoginController(Authenticator authenticator, LoginHandler loginHandler, ApplicationEventPublisher<ApplicationEvent> eventPublisher) {
        this.authenticator = authenticator;
        this.loginHandler = loginHandler;
        this.eventPublisher = eventPublisher;
    }

    @SingleResult
    @Post(consumes = {MediaType.TEXT_HTML, MediaType.MULTIPART_FORM_DATA}, produces = {MediaType.TEXT_HTML})
    Publisher<MutableHttpResponse<?>> login(@Nullable @Part String name, HttpRequest<?> request) {
        AuthenticationRequest<String, String> auth = new AuthenticationRequest<>() {
            @Override
            public String getIdentity() {
                return name;
            }

            @Override
            public String getSecret() {
                return "";
            }
        };

        return Flux.from(authenticator.authenticate(request, auth))
                .map(authenticationResponse -> {
                    if (authenticationResponse.isAuthenticated() && authenticationResponse.getAuthentication().isPresent()) {
                        Authentication authentication = authenticationResponse.getAuthentication().get();
                        eventPublisher.publishEvent(new LoginSuccessfulEvent(auth));
                        return loginHandler.loginSuccess(authentication, request);
                    } else {
                        this.eventPublisher.publishEvent(new LoginFailedEvent(auth));
                        return loginHandler.loginFailed(authenticationResponse, request);
                    }
                }).defaultIfEmpty(HttpResponse.status(HttpStatus.UNAUTHORIZED));
    }
}