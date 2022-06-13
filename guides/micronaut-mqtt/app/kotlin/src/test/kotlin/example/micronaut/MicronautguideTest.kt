package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.core.util.StringUtils.FALSE
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@Property(name = "mqtt.enabled", value = FALSE)
@MicronautTest
class MicronautguideTest {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Test
    fun testItWorks() {
        Assertions.assertTrue(application.isRunning)
    }
}
