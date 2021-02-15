package example.micronaut;

import io.micronaut.http.HttpHeaderValues;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.oauth2.endpoint.token.response.OauthUserDetailsMapper;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.views.View;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("/repos") // <1>
public class ReposController {

    public static final String CREATED = "created";
    public static final String DESC = "desc";
    public static final String REPOS = "repos";
    private final GithubApiClient githubApiClient;

    public ReposController(GithubApiClient githubApiClient) {
        this.githubApiClient = githubApiClient;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED) // <2>
    @View("repos") // <3>
    @Get // <4>
    Map<String, Object> index(Authentication authentication) {
        List<GithubRepo> repos = githubApiClient.repos(CREATED, DESC, authorizationValue(authentication));  // <5>
        Map<String, Object> model = new HashMap<>();
        model.put(REPOS, repos);
        return model;
    }

    private String authorizationValue(Authentication authentication) {
        String authorization = null;
        Object claim = authentication.getAttributes().get(OauthUserDetailsMapper.ACCESS_TOKEN_KEY);  // <6>
        if (claim instanceof String) {
            authorization = HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER + " " + claim.toString();
        }
        return authorization;
    }
}
