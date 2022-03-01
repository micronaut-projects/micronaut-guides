package example.micronaut

import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.BAD_REQUEST

@Property(name = 'spec.name', value = 'FruitControllerValidationSpec')
@MicronautTest
class FruitControllerValidationSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient httpClient

    void testFruitIsValidated() {
        when:
        httpClient.toBlocking().exchange(HttpRequest.POST('/fruits', new Fruit('', 'Hola')))

        then:
        HttpClientResponseException e = thrown()
        BAD_REQUEST == e.status
    }
}
