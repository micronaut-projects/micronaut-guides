package example.micronaut

import io.micronaut.context.BeanContext
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class ColorFetcherSpec extends Specification {

    @Inject
    BeanContext beanContext

    void beanOfTypeColorFetcherExists() {
        expect:
        beanContext.containsBean(ColorFetcher)
    }
}
