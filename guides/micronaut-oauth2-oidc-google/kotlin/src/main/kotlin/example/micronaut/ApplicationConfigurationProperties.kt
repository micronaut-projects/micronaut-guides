package example.micronaut

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Requires

@Requires(property = "app.hosted-domain")
@ConfigurationProperties("app")
class ApplicationConfigurationProperties : ApplicationConfiguration {
    override lateinit var hostedDomain: String
}
