package example.micronaut

import io.micronaut.context.BeanContext
import io.micronaut.context.annotation.Primary
import io.micronaut.inject.BeanDefinition
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.inject.Singleton
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class BasicCalculatorAnnotationSpec extends Specification {

    @Inject
    BeanContext beanContext

    void "BasicCalculator is annotated with @Primary"() {
        when:
        BeanDefinition<BasicCalculator> beanDefinition =
                beanContext.getBeanDefinition(BasicCalculator) // <2>

        then:
        beanDefinition.hasAnnotation(Primary) // <3>
        beanDefinition.hasStereotype(Singleton) // <4>
    }
}
