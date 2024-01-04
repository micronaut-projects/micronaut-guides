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

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Requires
import io.micronaut.security.oauth2.client.OpenIdProviderMetadata
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration
import io.micronaut.security.oauth2.endpoint.token.response.OpenIdClaims
import io.micronaut.security.oauth2.endpoint.token.response.validation.OpenIdClaimsValidator
import jakarta.inject.Singleton

@CompileStatic
@Requires(beans = ApplicationConfiguration)
@Singleton
class HostedDomainClaimValidator implements OpenIdClaimsValidator {

    public static final String HOSTED_DOMAIN_CLAIM = 'hd'

    private final String hostedDomain

    HostedDomainClaimValidator(ApplicationConfiguration applicationConfiguration) {
        this.hostedDomain = applicationConfiguration.hostedDomain
    }

    @Override
    boolean validate(OpenIdClaims claims,
                     OauthClientConfiguration clientConfiguration,
                     OpenIdProviderMetadata providerMetadata) {
        def hd = claims.get(HOSTED_DOMAIN_CLAIM)
        return hd instanceof String && ((String) hd).equalsIgnoreCase(hostedDomain)
    }
}
