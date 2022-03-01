package example.micronaut

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.OK

@MicronautTest // <1>
class LiquibaseEndpointSpec extends Specification {

    @Inject
    @Client('/')  // <2>
    HttpClient httpClient

    void migrationsAreExposedViaAndEndpoint() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        HttpResponse<LiquibaseReport> response = client.exchange(
                HttpRequest.GET('/liquibase'),
                LiquibaseReport)

        then:
        OK == response.status()

        when:
        LiquibaseReport liquibaseReport = response.body()

        then:
        2 == liquibaseReport?.changeSets?.size()
    }

    static class LiquibaseReport {
        List<ChangeSet> changeSets
    }

    static class ChangeSet {
        String id
    }
}
