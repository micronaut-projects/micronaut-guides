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
package example.micronaut.auth

import io.micronaut.core.annotation.Nullable
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import reactor.core.publisher.MonoSink

@Singleton // <1>
class CredentialsChecker(private val credentials: Credentials) : AuthenticationProvider<HttpRequest<*>> {
    override fun authenticate(
        @Nullable httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> {
        return Mono.create { emitter: MonoSink<AuthenticationResponse> ->
            if (authenticationRequest.identity == credentials.username && authenticationRequest.secret == credentials.password) {
                emitter.success(AuthenticationResponse.success(authenticationRequest.identity as String))
            } else {
                emitter.error(AuthenticationResponse.exception())
            }
        }
    }
}