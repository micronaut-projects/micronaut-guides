package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.context.scope.Refreshable
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class JsonWebKeysProviderSpec extends Specification {

    @Inject
    ApplicationContext applicationContext

    void jsonWebKeysProviderIsAnnotatedWithRefreshable() {
        expect:
        applicationContext.getBeanDefinition(JsonWebKeysProvider)
                .getAnnotationNameByStereotype(Refreshable)
                .isPresent()
    }
}
