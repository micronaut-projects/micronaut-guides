package example.micronaut

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest
class SaasSubscriptionControllerGetSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient

    void shouldReturnASaasSubscriptionWhenDataIsSaved() {
        when:
        def client = httpClient.toBlocking()
        HttpResponse<String> response = client.exchange("/subscriptions/99", String)

        then:
        response.status.code == HttpStatus.OK.code

        DocumentContext documentContext = JsonPath.parse(response.body())
        Number id = documentContext.read('$.id')
        String name = documentContext.read('$.name')
        Integer cents = documentContext.read('$.cents')

        id == 99
        name == "Advanced"
        cents == 2900
    }

    void shouldNotReturnASaasSubscriptionWithAnUnknownId() {
        given:
        def client = httpClient.toBlocking()

        when:
        client.exchange("/subscriptions/1000", String)

        then:
        HttpClientResponseException exception = thrown()
        exception.status.code == HttpStatus.NOT_FOUND.code
        exception.response.body.present
    }
}
