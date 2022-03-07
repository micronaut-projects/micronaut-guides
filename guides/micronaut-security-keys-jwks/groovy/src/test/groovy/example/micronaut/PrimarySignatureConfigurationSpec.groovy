package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.context.scope.Refreshable
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.core.annotation.AnnotationUtil.NAMED

@MicronautTest(startApplication = false)
class PrimarySignatureConfigurationSpec extends Specification {

    @Inject
    ApplicationContext applicationContext

    void primarySignatureConfigurationIsAnnotatedWithRefreshable() {
        expect:
        applicationContext.getBeanDefinition(PrimarySignatureConfiguration)
                .getAnnotationNameByStereotype(Refreshable)
                .isPresent()
    }

    void primarySignatureConfigurationIsAnnotatedNamedGenerator() {
        expect:
        applicationContext.getBeanDefinition(PrimarySignatureConfiguration).annotationMetadata
                .getAnnotation(NAMED)

        applicationContext.getBeanDefinition(PrimarySignatureConfiguration).annotationMetadata
                .getAnnotation(NAMED)
                .getValue(String)
                .isPresent()

        'generator' == applicationContext.getBeanDefinition(PrimarySignatureConfiguration).annotationMetadata
                .getAnnotation(NAMED)
                .getValue(String).get()
    }
}
