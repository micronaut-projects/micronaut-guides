package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest // <1>
class AccountControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <2>

    void "account requires authentication"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()
        HttpRequest<?> request = HttpRequest.GET("/account").accept(MediaType.TEXT_PLAIN)

        when:
        client.exchange(request)

        then:
        HttpClientResponseException ex = thrown()
        ex.status == HttpStatus.UNAUTHORIZED // <3>
    }
}
