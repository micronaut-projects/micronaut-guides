package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class JwkConfigurationSpec extends Specification {

    @Inject
    ApplicationContext applicationContext

    void beanOfTypeJwkConfigurationExists() {
        expect:
        applicationContext.containsBean(JwkConfiguration)
    }
}
