package example.micronaut
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.inject.Inject
import org.junit.jupiter.api.Assertions.assertTrue

@MicronautTest
class CompleteTest {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Test
    fun testItWorks() {
        assertTrue(application.isRunning)
    }

}