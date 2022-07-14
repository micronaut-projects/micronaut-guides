package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "endpoints.health.disk-space.threshold", value = "999999999999999999") // <1>
@MicronautTest
class PoorHealthSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    void "health endpoint exposes out of disc space"() {

        when:
        client.toBlocking().retrieve(HttpRequest.GET("/health"))

        then:
        HttpClientResponseException thrown = thrown()
        thrown.status == HttpStatus.SERVICE_UNAVAILABLE
        thrown.response.getBody(String).orElse("").contains("DOWN")
    }
}




