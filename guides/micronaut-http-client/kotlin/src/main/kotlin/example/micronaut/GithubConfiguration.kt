package example.micronaut

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Requires

@ConfigurationProperties(GithubConfiguration.PREFIX)
@Requires(property = GithubConfiguration.PREFIX)
class GithubConfiguration {
    var organization: String? = null
    var repo: String? = null
    var username: String? = null
    var token: String? = null

    companion object {
        const val PREFIX = "github"
    }
}
