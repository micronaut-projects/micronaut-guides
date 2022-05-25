package example.micronaut

import io.micronaut.test.extensions.spock.annotation.MicronautTest

@MicronautTest
class MicronautguideSpec extends BaseMongoDataSpec {

    void 'test it works'() {
        expect:
        application.running
    }

}
