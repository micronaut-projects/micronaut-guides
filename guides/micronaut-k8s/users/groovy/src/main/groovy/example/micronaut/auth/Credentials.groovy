package example.micronaut.auth

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("micronaut.authentication-credentials")
class Credentials {
    String username
    String password
}
