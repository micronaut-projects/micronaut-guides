package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.OK

@MicronautTest
class HealthSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient client

    void healthEndpointExposed() {
        when:
        HttpStatus status = client.toBlocking().retrieve(HttpRequest.GET('/health'), HttpStatus)

        then:
        OK == status
    }
}
