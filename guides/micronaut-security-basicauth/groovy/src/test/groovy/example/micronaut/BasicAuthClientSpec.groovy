package example.micronaut

import io.micronaut.test.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest // <1>
class BasicAuthClientSpec extends Specification {

    @Inject
    AppClient appClient // <2>

    def "Verify HTTP Basic Auth works"() {
        when:
        String credsEncoded = "sherlock:password".bytes.encodeBase64().toString()
        String rsp = appClient.home("Basic ${credsEncoded}") // <3>

        then:
        rsp == 'sherlock'
    }
}
