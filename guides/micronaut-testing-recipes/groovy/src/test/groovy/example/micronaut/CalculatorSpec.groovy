package example.micronaut

import io.micronaut.context.BeanContext
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class CalculatorSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "no multiple beans of type Calculator"() {
        when:
        Calculator calculator = beanContext.getBean(Calculator) // <2>

        then:
        noExceptionThrown()
        calculator instanceof BasicCalculator // <3>
        beanContext.getBeansOfType(Calculator).size() == 2 // <4>
    }
}
