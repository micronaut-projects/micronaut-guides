package example.micronaut

import io.micronaut.context.ApplicationContext
import spock.lang.AutoCleanup
import spock.lang.Specification

class CalculatorApplicationContextSpec extends Specification {

    @AutoCleanup
    ApplicationContext applicationContext = ApplicationContext.run() // <1>

    void "bean of type Calculator exists"() {
        expect:
        applicationContext.containsBean(Calculator) // <2>
    }
}
