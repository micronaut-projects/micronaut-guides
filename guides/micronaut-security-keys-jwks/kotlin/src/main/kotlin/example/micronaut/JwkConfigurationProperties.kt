package example.micronaut

import io.micronaut.context.annotation.ConfigurationProperties
import jakarta.validation.constraints.NotBlank

@ConfigurationProperties("jwk") // <1>
class JwkConfigurationProperties : JwkConfiguration {

    @NotBlank // <2>
    override lateinit var primary: String

    @NotBlank // <2>
    override lateinit var secondary: String
}
