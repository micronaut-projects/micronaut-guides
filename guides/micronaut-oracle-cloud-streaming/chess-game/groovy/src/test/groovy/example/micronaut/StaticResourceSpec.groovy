package example.micronaut;

import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import spock.lang.Specification
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject

@MicronautTest
class StaticResourceTest extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    void "static resource are exposed at /public"() {
        expect:
        HttpStatus.OK == client.toBlocking().exchange("/index.html").status()
    }
}
