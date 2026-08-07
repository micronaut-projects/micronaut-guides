package example.micronaut

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test

import static org.assertj.core.api.Assertions.assertThat

@MicronautTest // <1>
class SaasSubscriptionPostControllerTest {

    @Test
    void shouldCreateANewSaasSubscription(@Client('/') HttpClient httpClient) { // <2>
        BlockingHttpClient client = httpClient.toBlocking()
        SaasSubscription subscription = new SaasSubscription(id: 100L, name: 'Advanced', cents: 2900)

        HttpResponse<Void> createResponse = client.exchange(HttpRequest.POST('/subscriptions', subscription), Void)
        assertThat(createResponse.status.code).isEqualTo(HttpStatus.CREATED.code)
        Optional<URI> locationOfNewSaasSubscriptionOptional = createResponse.headers.get(HttpHeaders.LOCATION, URI)
        assertThat(locationOfNewSaasSubscriptionOptional).isPresent()

        URI locationOfNewSaasSubscription = locationOfNewSaasSubscriptionOptional.get()
        HttpResponse<String> getResponse = client.exchange(HttpRequest.GET(locationOfNewSaasSubscription), String)
        assertThat(getResponse.status.code).isEqualTo(HttpStatus.OK.code)

        DocumentContext documentContext = JsonPath.parse(getResponse.body())
        Number id = documentContext.read('$.id')
        assertThat(id).isNotNull()

        Integer cents = documentContext.read('$.cents')
        assertThat(cents).isEqualTo(2900)
    }
}
