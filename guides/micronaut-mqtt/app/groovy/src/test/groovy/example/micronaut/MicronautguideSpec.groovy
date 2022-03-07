package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.core.util.StringUtils.FALSE

@Property(name = 'mqtt.enabled', value = FALSE)
@MicronautTest
class MicronautguideSpec extends Specification {

    @Inject
    EmbeddedApplication<?> application

    void 'test it works'() {
        expect:
        application.running
    }
}
