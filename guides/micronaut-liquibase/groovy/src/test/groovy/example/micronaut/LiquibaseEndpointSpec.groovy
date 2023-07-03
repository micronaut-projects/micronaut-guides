package example.micronaut

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.serde.annotation.Serdeable
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
        HttpResponse<List<LiquibaseReport>> response = client.exchange(
                HttpRequest.GET('/liquibase'),
                Argument.listOf(LiquibaseReport)
        )

        then:
        OK == response.status()

        when:
        LiquibaseReport liquibaseReport = response.body().get(0)

        then:
        2 == liquibaseReport?.changeSets?.size()
    }

    @Serdeable
    static class LiquibaseReport {
        List<ChangeSet> changeSets
    }

    @Serdeable
    static class ChangeSet {
        String id
    }
}
