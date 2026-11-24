package example.micronaut

import io.micronaut.context.BeanContext
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class CalculatorBeanExistsSpec extends Specification {

    @Inject
    BeanContext beanContext // <2>

    void "bean of type Calculator exists"() {
        expect:
        beanContext.containsBean(Calculator) // <3>
    }
}
