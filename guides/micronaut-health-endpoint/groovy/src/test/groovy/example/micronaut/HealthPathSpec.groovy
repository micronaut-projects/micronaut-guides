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

@Property(name = "endpoints.all.path", value = "/endpoints/") // <1>
@MicronautTest
class HealthPathSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    void "health endpoint exposed at non-default endpoints path"() {
        when:
        HttpStatus status = client.toBlocking().retrieve(HttpRequest.GET("/endpoints/health"), HttpStatus) // <2>

        then:
        status == HttpStatus.OK

        when:
        client.toBlocking().retrieve(HttpRequest.GET("/health"), HttpStatus) // <3>

        then:
        HttpClientResponseException thrown = thrown()
        thrown.status == HttpStatus.NOT_FOUND
    }
}
