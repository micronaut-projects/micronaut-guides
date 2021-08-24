package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.http.HttpHeaderValues
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View

import static io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper.ACCESS_TOKEN_KEY

@CompileStatic
@Controller('/repos') // <1>
class ReposController {

    public static final String CREATED = 'created'
    public static final String DESC = 'desc'
    public static final String REPOS = 'repos'

    private final GithubApiClient githubApiClient

    ReposController(GithubApiClient githubApiClient) {
        this.githubApiClient = githubApiClient
    }

    @Secured(SecurityRule.IS_AUTHENTICATED) // <2>
    @View('repos') // <3>
    @Get // <4>
    Map<String, Object> index(Authentication authentication) {
        List<GithubRepo> repos = githubApiClient.repos(CREATED, DESC, authorizationValue(authentication))  // <5>
        return [(REPOS): repos] as Map
    }

    private String authorizationValue(Authentication authentication) {
        Object claim = authentication.attributes[ACCESS_TOKEN_KEY]  // <6>
        if (claim instanceof String) {
            return HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER + ' ' + claim
        }
        return null
    }
}
