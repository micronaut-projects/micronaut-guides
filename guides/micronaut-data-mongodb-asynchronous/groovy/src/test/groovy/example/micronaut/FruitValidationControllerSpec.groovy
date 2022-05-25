package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest

@MicronautTest
class FruitValidationControllerSpec extends BaseMongoDataSpec {

    def "fruit is validated"() {
        when:
        httpClient.toBlocking().exchange(HttpRequest.POST('/fruits', new Fruit('', 'Hola')))

        then:
        HttpClientResponseException e = thrown()
        e.status == HttpStatus.BAD_REQUEST
    }
}
