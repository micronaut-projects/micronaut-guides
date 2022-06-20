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
