package example.micronaut;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.oauth2.configuration.OauthClientConfiguration;
import io.micronaut.security.rules.SecurityRule;

import javax.inject.Named;

@Controller
public class ClientIdController {

    private final OauthClientConfiguration oauthClientConfiguration;

    public ClientIdController(@Named("companyauthserver") OauthClientConfiguration oauthClientConfiguration) {
        this.oauthClientConfiguration = oauthClientConfiguration;
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Produces(MediaType.TEXT_PLAIN)
    @Get
    public String index() {
        return oauthClientConfiguration.getClientId();
    }
}
