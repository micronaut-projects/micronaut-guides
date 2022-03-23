package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.core.annotation.AnnotationUtil.NAMED
import io.micronaut.runtime.context.scope.Refreshable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false)
class PrimarySignatureConfigurationTest {

    @Inject
    lateinit var applicationContext: ApplicationContext

    @Test
    fun primarySignatureConfigurationIsAnnotatedWithRefreshable() {
        assertTrue(applicationContext.getBeanDefinition(PrimarySignatureConfiguration::class.java)
                .getAnnotationNameByStereotype(Refreshable::class.java)
                .isPresent)
    }

    @Test
    fun primarySignatureConfigurationIsAnnotatedNamedGenerator() {
        assertNotNull(applicationContext.getBeanDefinition(PrimarySignatureConfiguration::class.java).annotationMetadata
                .getAnnotation<Annotation>(NAMED))

        assertTrue(applicationContext.getBeanDefinition(PrimarySignatureConfiguration::class.java).annotationMetadata
                .getAnnotation<Annotation>(NAMED)
                .getValue(String::class.java).isPresent)

        assertEquals("generator", applicationContext.getBeanDefinition(PrimarySignatureConfiguration::class.java).annotationMetadata
                .getAnnotation<Annotation>(NAMED)
                .getValue(String::class.java).get())
    }
}
