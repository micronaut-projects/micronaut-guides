package example.micronaut

import io.micronaut.context.ApplicationContext
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(startApplication = false) // <1>
class ActiveEnvironmentsSpec extends Specification {

    @Inject
    ApplicationContext applicationContext // <2>

    void "test environment is active by default"() {
        expect:
        applicationContext.environment.activeNames == ["test"] as Set // <3>
    }
}
