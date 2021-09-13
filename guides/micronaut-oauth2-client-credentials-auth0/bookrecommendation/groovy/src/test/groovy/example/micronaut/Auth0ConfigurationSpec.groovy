package example.micronaut

import io.micronaut.context.BeanContext
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false)
class Auth0ConfigurationSpec extends Specification {
    @Inject
    BeanContext beanContext

    void "bean of type Auth0Configuration exists"() {
        expect:
        'https://micronautguides.eu.auth0.com/api/v2/' ==
                beanContext.getBean(Auth0Configuration).apiIdentifier
    }
}
