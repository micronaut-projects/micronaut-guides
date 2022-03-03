package example.micronaut

import groovy.transform.CompileStatic
import io.micronaut.context.annotation.ConfigurationProperties

@CompileStatic
@ConfigurationProperties("application") // <1>
class ApplicationConfigurationProperties implements ApplicationConfiguration {

    private final int DEFAULT_MAX = 10

    int max = DEFAULT_MAX
}
