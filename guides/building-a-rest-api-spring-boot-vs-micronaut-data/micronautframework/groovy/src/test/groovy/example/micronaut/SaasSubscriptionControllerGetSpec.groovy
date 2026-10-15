package example.micronaut

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Sql(value = ['classpath:schema.sql', 'classpath:data.sql']) // <1>
@MicronautTest // <2>
class SaasSubscriptionControllerGetSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <3>

    void "should return a SaaS subscription when data is saved"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        HttpResponse<String> response = client.exchange('/subscriptions/99', String)

        then:
        response.status.code == HttpStatus.OK.code

        when:
        DocumentContext documentContext = JsonPath.parse(response.body())
        Number id = documentContext.read('$.id')
        String name = documentContext.read('$.name')
        Integer cents = documentContext.read('$.cents')

        then:
        id
        id == 99
        name
        name == 'Advanced'
        cents == 2900
    }

    void "should not return a SaaS subscription with an unknown id"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        client.exchange('/subscriptions/1000', String)

        then:
        HttpClientResponseException thrown = thrown() // <4>
        thrown.status.code == HttpStatus.NOT_FOUND.code
        thrown.response.body.isEmpty()
    }
}
