package example.micronaut.auth

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("authentication-credentials")
class Credentials {
    String username
    String password
}
