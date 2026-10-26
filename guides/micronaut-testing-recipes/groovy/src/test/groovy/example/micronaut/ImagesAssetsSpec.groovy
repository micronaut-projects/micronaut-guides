package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest // <1>
class ImagesAssetsSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <2>

    void "images are accessible"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        client.exchange(HttpRequest.GET("/assets/images/micronaut_stacked_black.png")) // <3>

        then:
        noExceptionThrown()
    }
}
