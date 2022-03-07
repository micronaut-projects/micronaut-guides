package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.context.scope.Refreshable
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class SecondarySignatureConfigurationSpec extends Specification {

    @Inject
    ApplicationContext applicationContext

    void secondarySignatureConfigurationIsAnnotatedWithRefreshable() {
        expect:
        applicationContext.getBeanDefinition(SecondarySignatureConfiguration)
                .getAnnotationNameByStereotype(Refreshable)
                .isPresent()
    }
}
