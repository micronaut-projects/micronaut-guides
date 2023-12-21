/*
 * Copyright 2017-2023 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut.advanced.auth;

import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.event.ApplicationEvent;
import io.micronaut.context.event.ApplicationEventPublisher;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.endpoints.LogoutController;
import io.micronaut.security.event.LogoutEvent;
import io.micronaut.security.handlers.LogoutHandler;

import java.security.Principal;

@Replaces(LogoutController.class)
@Controller("/signout")
class AuthLogoutController {

    private final LogoutHandler<HttpRequest<?>, MutableHttpResponse<?>> logoutHandler;
    private final ApplicationEventPublisher<ApplicationEvent> eventPublisher;

    AuthLogoutController(LogoutHandler<HttpRequest<?>, MutableHttpResponse<?>> logoutHandler, ApplicationEventPublisher<ApplicationEvent> eventPublisher) {
        this.logoutHandler = logoutHandler;
        this.eventPublisher = eventPublisher;
    }

    @Post(consumes = {MediaType.TEXT_HTML, MediaType.APPLICATION_FORM_URLENCODED}, produces = {MediaType.TEXT_HTML})
    MutableHttpResponse<?> logout(HttpRequest<?> request, @Nullable Principal principal) {
        if (principal != null) {
            eventPublisher.publishEvent(new LogoutEvent(principal));
        }
        return logoutHandler.logout(request);
    }
}