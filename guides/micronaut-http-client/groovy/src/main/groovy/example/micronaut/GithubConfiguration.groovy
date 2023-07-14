package example.micronaut

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Requires
import io.micronaut.core.annotation.Nullable

@ConfigurationProperties(GithubConfiguration.PREFIX)
@Requires(property = GithubConfiguration.PREFIX)
class GithubConfiguration {
    public static final String PREFIX = "github"

    String organization

    String repo

    @Nullable
    String username

    @Nullable
    String token
}
