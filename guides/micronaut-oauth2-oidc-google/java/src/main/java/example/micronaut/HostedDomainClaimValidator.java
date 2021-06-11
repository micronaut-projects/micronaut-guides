package example.micronaut;

import io.micronaut.context.annotation.Requires;
import io.micronaut.security.oauth2.client.OpenIdProviderMetadata;
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration;
import io.micronaut.security.oauth2.endpoint.token.response.OpenIdClaims;
import io.micronaut.security.oauth2.endpoint.token.response.validation.OpenIdClaimsValidator;

import jakarta.inject.Singleton;

@Requires(beans = ApplicationConfiguration.class)
@Singleton
public class HostedDomainClaimValidator implements OpenIdClaimsValidator {

    public static final String HOSTED_DOMAIN_CLAIM = "hd";

    private final String hostedDomain;

    public HostedDomainClaimValidator(ApplicationConfiguration applicationConfiguration) {
        this.hostedDomain = applicationConfiguration.getHostedDomain();
    }

    @Override
    public boolean validate(OpenIdClaims claims,
                            OauthClientConfiguration clientConfiguration,
                            OpenIdProviderMetadata providerMetadata) {
        Object hd = claims.get(HOSTED_DOMAIN_CLAIM);
        return hd instanceof String && ((String) hd).equalsIgnoreCase(hostedDomain);
    }
}
