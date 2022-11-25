package example.micronaut.auth;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("micronaut.authentication-credentials")
public record Credentials (
        String username,
        String password
) {
}
