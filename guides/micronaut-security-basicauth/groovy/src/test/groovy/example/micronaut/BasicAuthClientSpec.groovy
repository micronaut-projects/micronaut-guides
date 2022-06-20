package example.micronaut

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest // <1>
class BasicAuthClientSpec extends Specification {

    @Inject
    AppClient appClient // <2>

    void "Verify HTTP Basic Auth works"() {
        when:
        String credsEncoded = "sherlock:password".bytes.encodeBase64()
        String rsp = appClient.home("Basic $credsEncoded") // <3>

        then:
        rsp == 'sherlock'
    }
}
