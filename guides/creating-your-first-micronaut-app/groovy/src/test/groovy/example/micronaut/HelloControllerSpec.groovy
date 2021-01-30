package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest // <1>
class HelloControllerSpec extends Specification {

    @Inject
    @Client("/")  // <2>
    RxHttpClient client

    void "test hello world response"() {
        when:
        HttpRequest request = HttpRequest.GET('/hello')  // <3>
        String rsp = client.toBlocking().retrieve(request)

        then:
        rsp == "Hello World"
    }
}