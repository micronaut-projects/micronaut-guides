package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Requires

@ConfigurationProperties(GithubConfiguration.PREFIX)
@Requires(property = GithubConfiguration.PREFIX)
@CompileStatic
class GithubConfiguration {

    public static final String PREFIX = "github"
    public static final String GITHUB_API_URL = "https://api.github.com"

    String organization
    String repo
    String username
    String token
}
