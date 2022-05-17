package example.micronaut

import io.micronaut.context.BeanContext
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false)
class ColorFetcherTest {

    @Inject
    lateinit var beanContext: BeanContext

    @Test
    fun beanOfTypeColorFetcherExists() {
        assertTrue(beanContext.containsBean(ColorFetcher::class.java))
    }
}
