package example.micronaut

import io.micronaut.context.BeanContext
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest(startApplication = false) // <1>
class CompositeColorFetcherTest {

    @Inject
    lateinit var beanContext: BeanContext

    @Test
    fun compositeColorFetcherIsThePrimaryBeanOfTypeColorFetcher() {
        assertTrue(beanContext.getBean(ColorFetcher::class.java) is CompositeColorFetcher)
    }
}
