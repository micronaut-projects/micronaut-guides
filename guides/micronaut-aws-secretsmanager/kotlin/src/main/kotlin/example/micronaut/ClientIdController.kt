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

import io.micronaut.security.annotation.Secured
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration
import io.micronaut.security.rules.SecurityRule
import jakarta.inject.Named
import io.micronaut.http.annotation.Get
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Produces
import io.micronaut.http.annotation.Controller

@Controller
class ClientIdController(@Named("companyauthserver") oauthClientConfiguration: OauthClientConfiguration) {
    private val oauthClientConfiguration: OauthClientConfiguration

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Produces(MediaType.TEXT_PLAIN)
    @Get
    fun index(): String {
        return oauthClientConfiguration.getClientId()
    }

    init {
        this.oauthClientConfiguration = oauthClientConfiguration
    }
}