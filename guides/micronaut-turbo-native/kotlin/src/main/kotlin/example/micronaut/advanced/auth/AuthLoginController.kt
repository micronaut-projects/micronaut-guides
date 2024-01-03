/*
 * Copyright 2017-2024 original authors
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
package example.micronaut.advanced.auth

import io.micronaut.context.annotation.Replaces
import io.micronaut.context.event.ApplicationEvent
import io.micronaut.context.event.ApplicationEventPublisher
import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Part
import io.micronaut.http.annotation.Post
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.Authenticator
import io.micronaut.security.endpoints.LoginController
import io.micronaut.security.event.LoginFailedEvent
import io.micronaut.security.event.LoginSuccessfulEvent
import io.micronaut.security.handlers.LoginHandler
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

@Replaces(LoginController::class)
@Controller("/login")
class AuthLoginController(
    val authenticator: Authenticator<HttpRequest<*>>,
    val loginHandler: LoginHandler<HttpRequest<*>, MutableHttpResponse<*>>,
    val eventPublisher: ApplicationEventPublisher<ApplicationEvent>
) {

    @SingleResult
    @Post(consumes = [MediaType.TEXT_HTML, MediaType.MULTIPART_FORM_DATA], produces = [MediaType.TEXT_HTML])
    fun login(@Part name: String?, request: HttpRequest<*>?): Publisher<MutableHttpResponse<*>> {
        val auth = object: AuthenticationRequest<String, String> {
            override fun getIdentity() = name ?: ""
            override fun getSecret() = ""
        }
        return Flux.from(authenticator.authenticate(request, auth))
            .map { authenticationResponse: AuthenticationResponse ->
                if (authenticationResponse.isAuthenticated && authenticationResponse.authentication.isPresent) {
                    val authentication = authenticationResponse.authentication.get()
                    eventPublisher.publishEvent(LoginSuccessfulEvent(auth))
                    return@map loginHandler.loginSuccess(authentication, request)
                } else {
                    this.eventPublisher.publishEvent(LoginFailedEvent(auth))
                    return@map loginHandler.loginFailed(authenticationResponse, request)
                }
            }.defaultIfEmpty(HttpResponse.status(HttpStatus.UNAUTHORIZED))
    }
}