package example.micronaut.auth

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("authentication-credentials")
class Credentials{
    lateinit var username: String
    lateinit var password: String
}
