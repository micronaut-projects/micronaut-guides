package example.micronaut.auth;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("authentication-credentials") // <1>
public record Credentials (String username, String password) {}
