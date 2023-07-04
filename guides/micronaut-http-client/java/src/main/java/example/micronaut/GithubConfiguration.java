package example.micronaut;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Nullable;

@ConfigurationProperties(GithubConfiguration.PREFIX)
@Requires(property = GithubConfiguration.PREFIX)
public record GithubConfiguration(String organization,
                                  String repo,
                                  @Nullable String username,
                                  @Nullable String token) {
    public static final String PREFIX = "github";
}
