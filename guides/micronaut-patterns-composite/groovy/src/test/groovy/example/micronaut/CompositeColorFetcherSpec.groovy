package example.micronaut

import io.micronaut.context.BeanContext
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class CompositeColorFetcherSpec extends Specification {

    @Inject
    BeanContext beanContext

    void compositeColorFetcherIsThePrimaryBeanOfTypeColorFetcher() {
        expect:
        beanContext.getBean(ColorFetcher) instanceof CompositeColorFetcher
    }
}
