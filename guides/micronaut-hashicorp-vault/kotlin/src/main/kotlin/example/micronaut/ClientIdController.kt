package example.micronaut

import io.micronaut.security.annotation.Secured
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration
import io.micronaut.security.rules.SecurityRule
import io.micronaut.http.annotation.Get
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Produces
import jakarta.inject.Named

@Controller
class ClientIdController(
    @Named("companyauthserver") val oauthClientConfiguration: OauthClientConfiguration // <1>
) {

    @Get
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Produces(MediaType.TEXT_PLAIN)
    fun index(): String {
        return oauthClientConfiguration.clientId
    }
}