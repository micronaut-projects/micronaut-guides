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

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationFailureReason
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider
import jakarta.inject.Singleton

@Singleton // <1>
class AuthenticationProviderUserPassword<B>(private val credentials: Credentials) : HttpRequestAuthenticationProvider<B> { // <2>

    override fun authenticate(
        httpRequest: HttpRequest<B>?,
        authenticationRequest: AuthenticationRequest<String, String>
    ): AuthenticationResponse {
        return if (authenticationRequest.identity == credentials.username && authenticationRequest.secret == credentials.password)
            AuthenticationResponse.success(authenticationRequest.identity) else
            AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)
    }
}
