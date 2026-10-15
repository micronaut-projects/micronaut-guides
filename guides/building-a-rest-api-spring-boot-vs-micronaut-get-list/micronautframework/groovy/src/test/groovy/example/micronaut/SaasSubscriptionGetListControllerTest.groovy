package example.micronaut

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import net.minidev.json.JSONArray
import org.junit.jupiter.api.Test

import static org.assertj.core.api.Assertions.assertThat

@Sql(value = ['classpath:schema.sql', 'classpath:data.sql']) // <1>
@MicronautTest // <2>
class SaasSubscriptionGetListControllerTest {

    @Inject
    @Client('/')
    HttpClient httpClient // <3>

    @Test
    void shouldReturnASortedPageOfSaasSubscriptions() {
        BlockingHttpClient client = httpClient.toBlocking()
        URI uri = UriBuilder.of('/subscriptions')
                .queryParam('page', 0)
                .queryParam('size', 1)
                .queryParam('sort', 'cents,desc')
                .build()
        HttpResponse<String> response = client.exchange(HttpRequest.GET(uri), String)
        assertThat(response.status.code).isEqualTo(HttpStatus.OK.code)

        DocumentContext documentContext = JsonPath.parse(response.body())
        JSONArray page = documentContext.read('$[*]')
        assertThat(page).hasSize(1)

        Integer cents = documentContext.read('$[0].cents')
        assertThat(cents).isEqualTo(4900)
    }

    @Test
    void shouldReturnAPageOfSaasSubscriptions() {
        BlockingHttpClient client = httpClient.toBlocking()
        URI uri = UriBuilder.of('/subscriptions')
                .queryParam('page', 0)
                .queryParam('size', 1)
                .build()
        HttpResponse<String> response = client.exchange(HttpRequest.GET(uri), String)
        assertThat(response.status.code).isEqualTo(HttpStatus.OK.code)

        DocumentContext documentContext = JsonPath.parse(response.body())
        JSONArray page = documentContext.read('$[*]')
        assertThat(page.size()).isEqualTo(1)
    }

    @Test
    void shouldReturnAllSaasSubscriptionsWhenListIsRequested() {
        BlockingHttpClient client = httpClient.toBlocking()
        HttpResponse<String> response = client.exchange('/subscriptions', String)
        assertThat(response.status.code).isEqualTo(HttpStatus.OK.code)

        DocumentContext documentContext = JsonPath.parse(response.body())
        int saasSubscriptionCount = documentContext.read('$.length()')
        assertThat(saasSubscriptionCount).isEqualTo(3)

        JSONArray ids = documentContext.read('$..id')
        assertThat(ids).containsExactlyInAnyOrder(99, 100, 101)

        JSONArray cents = documentContext.read('$..cents')
        assertThat(cents).containsExactlyInAnyOrder(1400, 2900, 4900)
    }
}
