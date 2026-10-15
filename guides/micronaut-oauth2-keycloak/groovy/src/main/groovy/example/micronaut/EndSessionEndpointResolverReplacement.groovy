/*
 * Copyright 2017-2026 original authors
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
import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Replaces
import io.micronaut.security.config.SecurityConfiguration
import io.micronaut.security.oauth2.client.OpenIdProviderMetadata
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration
import io.micronaut.security.oauth2.endpoint.endsession.request.EndSessionEndpoint
import io.micronaut.security.oauth2.endpoint.endsession.request.EndSessionEndpointResolver
import io.micronaut.security.oauth2.endpoint.endsession.request.OktaEndSessionEndpoint
import io.micronaut.security.oauth2.endpoint.endsession.response.EndSessionCallbackUrlBuilder
import io.micronaut.security.token.reader.TokenResolver
import jakarta.inject.Singleton

import java.util.function.Supplier

@CompileStatic
@Singleton
@Replaces(EndSessionEndpointResolver)
class EndSessionEndpointResolverReplacement extends EndSessionEndpointResolver {
    private final TokenResolver tokenResolver
    private final SecurityConfiguration securityConfiguration

    EndSessionEndpointResolverReplacement(BeanContext beanContext,
                                          SecurityConfiguration securityConfiguration,
                                          TokenResolver tokenResolver) {
        super(beanContext)
        this.tokenResolver = tokenResolver
        this.securityConfiguration = securityConfiguration
    }

    @Override
    Optional<EndSessionEndpoint> resolve(OauthClientConfiguration oauthClientConfiguration,
                                         Supplier<OpenIdProviderMetadata> openIdProviderMetadata,
                                         EndSessionCallbackUrlBuilder endSessionCallbackUrlBuilder) {
        EndSessionEndpoint endSessionEndpoint = new OktaEndSessionEndpoint(endSessionCallbackUrlBuilder,
                oauthClientConfiguration,
                openIdProviderMetadata,
                securityConfiguration,
                tokenResolver)
        Optional.of(endSessionEndpoint)
    }
}
