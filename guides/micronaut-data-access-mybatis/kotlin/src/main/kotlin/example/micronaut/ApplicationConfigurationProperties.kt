package example.micronaut

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("application") // <1>
class ApplicationConfigurationProperties : ApplicationConfiguration {

    private val DEFAULT_MAX = 10

    override var max = DEFAULT_MAX
}
