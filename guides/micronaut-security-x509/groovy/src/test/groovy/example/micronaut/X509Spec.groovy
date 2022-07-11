package example.micronaut

import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest // <1>
class X509Spec extends Specification {

    @Inject
    @Client('/') // <2>
    HttpClient httpClient

    void 'test client cert'() {
        given:
        String expected = 'Hello myusername (X.509 cert issued by CN=micronaut.guide.x509)'

        when:
        String response = httpClient.toBlocking().retrieve('/') // <3>

        then:
        expected == response // <4>
    }
}
