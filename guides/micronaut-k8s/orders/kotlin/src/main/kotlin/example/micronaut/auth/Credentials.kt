package example.micronaut.auth

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("authentication-credentials") // <1>
class Credentials{
    lateinit var username: String
    lateinit var password: String
}
