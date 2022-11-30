package example.micronaut.auth;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("authentication-credentials")
public record Credentials (
        String username,
        String password
) {
}
