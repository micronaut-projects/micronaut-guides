package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest // <1>
class PlanControllerRequiresAuthenticationSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <2>

    void "plan controller requires authentication"() {
        when:
        httpClient.toBlocking().exchange(HttpRequest.GET("/plan").accept(MediaType.TEXT_PLAIN))

        then:
        HttpClientResponseException ex = thrown()
        ex.status == HttpStatus.UNAUTHORIZED
    }
}
