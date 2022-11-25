package example.micronaut.auth

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("micronaut.authentication-credentials")
class Credentials{
    lateinit var username: String
    lateinit var password: String
}
