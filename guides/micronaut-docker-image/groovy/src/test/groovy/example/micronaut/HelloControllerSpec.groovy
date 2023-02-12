package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification
import spock.lang.Ignore

@Ignore
@MicronautTest
class HelloControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    void "apex returns JSON"() {
        when:
        String body = client.toBlocking().retrieve(HttpRequest.GET("/"));
        then:
        body
        "{\"message\":\"Hello World\"}" == body
    }
}