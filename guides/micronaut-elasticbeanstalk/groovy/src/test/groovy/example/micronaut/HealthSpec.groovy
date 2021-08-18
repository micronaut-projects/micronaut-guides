package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest // <1>
class HealthSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client // <2>

    void "/health responds 200"()  {
        when:
        Map m = client.toBlocking().retrieve(HttpRequest.GET("/health"), Map) // <3>

        then:
        noExceptionThrown()
        m
        m.containsKey("status")
        m["status"] == "UP"
    }
}