package example.micronaut

import io.micronaut.runtime.EmbeddedApplication
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class MicronautguideTest : BaseMysqlTest() {
    @Inject
    override var application: EmbeddedApplication<*>? = null
    @Test
    fun testItWorks() {
        Assertions.assertTrue(application!!.isRunning)
    }
}