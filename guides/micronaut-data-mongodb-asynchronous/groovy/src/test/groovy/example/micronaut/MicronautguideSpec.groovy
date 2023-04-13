package example.micronaut

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest(transactional = false)
class MicronautguideSpec extends Specification {

    @Inject
    EmbeddedApplication<?> application

    void "test it works"() {
        expect:
        application.isRunning()
    }
}
