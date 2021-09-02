package example.micronaut

import io.micronaut.http.HttpHeaderValues
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper.ACCESS_TOKEN_KEY
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View

@Controller("/repos") // <1>
class ReposController(private val githubApiClient: GithubApiClient) {

    @Secured(SecurityRule.IS_AUTHENTICATED) // <2>
    @View("repos") // <3>
    @Get // <4>
    fun index(authentication: Authentication): Map<String, Any> {
        val repos = githubApiClient.repos(CREATED, DESC, authorizationValue(authentication)) // <5>
        return mutableMapOf(REPOS to repos)
    }

    private fun authorizationValue(authentication: Authentication): String? {
        val claim = authentication.attributes[ACCESS_TOKEN_KEY] // <6>
        return if (claim is String) {
            HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER + ' ' + claim
        } else null
    }

    companion object {
        const val CREATED = "created"
        const val DESC = "desc"
        const val REPOS = "repos"
    }
}
