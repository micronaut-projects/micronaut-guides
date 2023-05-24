package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest // <1>
class HelloControllerSpec extends Specification {

    @Inject
    @Client("/") // <2>
    HttpClient client

    void "test hello"() {
        when:
        String body = client.toBlocking().retrieve(HttpRequest.GET("/")); // <3>

        then:
        body == '{"message":"Hello World"}'
    }
}