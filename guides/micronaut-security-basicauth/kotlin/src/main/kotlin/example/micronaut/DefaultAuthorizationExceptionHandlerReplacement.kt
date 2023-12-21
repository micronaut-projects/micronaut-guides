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
package example.micronaut

import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpHeaders.WWW_AUTHENTICATE
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus.FORBIDDEN
import io.micronaut.http.HttpStatus.UNAUTHORIZED
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor
import io.micronaut.security.authentication.AuthorizationException
import io.micronaut.security.authentication.DefaultAuthorizationExceptionHandler
import io.micronaut.security.config.RedirectConfiguration
import io.micronaut.security.config.RedirectService
import io.micronaut.security.errors.PriorToLoginPersistence
import jakarta.inject.Singleton

@Singleton // <1>
@Replaces(DefaultAuthorizationExceptionHandler::class) // <2>
class DefaultAuthorizationExceptionHandlerReplacement(
    errorResponseProcessor: ErrorResponseProcessor<*>,
    redirectConfiguration: RedirectConfiguration,
    redirectService: RedirectService,
    priorToLoginPersistence: PriorToLoginPersistence<*, *>?
) : DefaultAuthorizationExceptionHandler(
    errorResponseProcessor, redirectConfiguration, redirectService, priorToLoginPersistence
) {

    override fun httpResponseWithStatus(
        request: HttpRequest<*>,
        e: AuthorizationException
    ): MutableHttpResponse<*> =
        if (e.isForbidden) {
            HttpResponse.status<Any>(FORBIDDEN);
        } else {
            HttpResponse.status<Any>(UNAUTHORIZED)
                .header(WWW_AUTHENTICATE, "Basic realm=\"Micronaut Guide\"")
        }
}
