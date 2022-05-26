package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject

@MicronautTest // <1>
class FruitDuplicationExceptionHandlerSpec extends BaseSpec {

    @Inject
    @Client("/")
    HttpClient httpClient // <2>

    void "duplicated fruit returns 422"() {
        when:
        FruitCommand banana = new FruitCommand("Banana")
        HttpRequest<?> request = HttpRequest.POST("/fruits", banana)
        HttpResponse<?> response = httpClient.toBlocking().exchange(request)

        then:
        HttpStatus.CREATED == response.status()

        when:
        httpClient.toBlocking().exchange(request)

        then:
        HttpClientResponseException e = thrown()
        HttpStatus.UNPROCESSABLE_ENTITY == e.status

        when:
        HttpRequest<?> deleteRequest = HttpRequest.DELETE("/fruits", banana)
        HttpResponse<?> deleteResponse = httpClient.toBlocking().exchange(deleteRequest)

        then:
        HttpStatus.NO_CONTENT == deleteResponse.status()
    }
}
