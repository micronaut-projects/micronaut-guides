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
package example.micronaut

import io.micronaut.context.annotation.Requires
import io.micronaut.security.oauth2.client.OpenIdProviderMetadata
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration
import io.micronaut.security.oauth2.endpoint.token.response.OpenIdClaims
import io.micronaut.security.oauth2.endpoint.token.response.validation.OpenIdClaimsValidator
import jakarta.inject.Singleton

@Requires(beans = [ApplicationConfiguration::class])
@Singleton
class HostedDomainClaimValidator(applicationConfiguration: ApplicationConfiguration) : OpenIdClaimsValidator {

    private val hostedDomain: String
    init {
        hostedDomain = applicationConfiguration.hostedDomain
    }

    override fun validate(claims: OpenIdClaims,
                          clientConfiguration: OauthClientConfiguration,
                          providerMetadata: OpenIdProviderMetadata): Boolean {
        val hd = claims[HOSTED_DOMAIN_CLAIM]
        return hd is String && hd.equals(hostedDomain, ignoreCase = true)
    }

    companion object {
        const val HOSTED_DOMAIN_CLAIM = "hd"
    }
}
