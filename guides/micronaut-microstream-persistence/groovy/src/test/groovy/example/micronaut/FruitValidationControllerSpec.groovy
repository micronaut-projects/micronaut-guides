package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest

@MicronautTest
class FruitValidationControllerSpec extends BaseMicroStreamSpec {

    void "fruit validation works as expected"() {
        when:
        httpClient.toBlocking().exchange(HttpRequest.POST("/fruits", new FruitCommand("", "")))

        then:
        def e = thrown(HttpClientResponseException.class)
        e.status == HttpStatus.BAD_REQUEST
    }
}
