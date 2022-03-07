package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.runtime.context.scope.Refreshable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false)
class JsonWebKeysProviderTest {

    @Inject
    lateinit var applicationContext: ApplicationContext

    @Test
    fun jsonWebKeysProviderIsAnnotatedWithRefreshable() {
        assertTrue(applicationContext.getBeanDefinition(JsonWebKeysProvider::class.java)
                .getAnnotationNameByStereotype(Refreshable::class.java)
                .isPresent)
    }
}
