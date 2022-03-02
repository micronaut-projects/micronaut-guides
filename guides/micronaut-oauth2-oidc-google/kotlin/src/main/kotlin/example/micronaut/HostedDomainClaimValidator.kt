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
