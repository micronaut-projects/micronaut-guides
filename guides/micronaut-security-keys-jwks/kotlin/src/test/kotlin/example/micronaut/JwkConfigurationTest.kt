package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false)
class JwkConfigurationTest {

    @Inject
    lateinit var applicationContext: ApplicationContext

    @Test
    fun beanOfTypeJwkConfigurationExists() {
        assertTrue(applicationContext.containsBean(JwkConfiguration::class.java))
    }
}
