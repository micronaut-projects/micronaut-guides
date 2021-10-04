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