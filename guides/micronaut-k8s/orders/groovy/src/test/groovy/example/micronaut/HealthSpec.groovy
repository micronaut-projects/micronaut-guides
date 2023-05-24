package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest // <1>
class HealthTest extends Specification {

    @Inject
    @Client("/")
    HttpClient client // <2>

    void 'health endpoint exposed'() {
        when:
        def status = client.toBlocking().retrieve(HttpRequest.GET("/health"), HttpStatus.class)

        then:
        status == HttpStatus.OK
    }

}
