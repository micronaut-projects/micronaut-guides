package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest // <1>
class HealthSpec extends Specification {

    @Inject
    @Client("/") // <2>
    HttpClient client

    void "health endpoint exposed" () {
        when:
        HttpStatus status = client.toBlocking().retrieve(HttpRequest.GET("/health"), HttpStatus) // <3>

        then:
        status == HttpStatus.OK
    }
}
