package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.core.util.StringUtils
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = "micronaut.http.client.follow-redirects", value = StringUtils.FALSE) // <1>
@MicronautTest // <2>
class HomeControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <3>

    void "home redirects to contacts"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        HttpResponse<?> response = client.exchange(HttpRequest.GET("/"))

        then:
        noExceptionThrown()
        response.status() == HttpStatus.SEE_OTHER // <4>
        response.headers.get(HttpHeaders.LOCATION) == "/contacts" // <5>
    }
}
