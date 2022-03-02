package example.micronaut

import io.micronaut.core.type.Argument
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
class FlywayEndpointSpec extends Specification {

    @Inject
    @Client('/')  // <2>
    HttpClient httpClient

    void migrationsAreExposedViaAndEndpoint() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()

        when:
        HttpResponse<List<FlywayReport>> response = client.exchange(
                HttpRequest.GET('/flyway'),
                Argument.listOf(FlywayReport))

        then:
        OK == response.status()

        when:
        List<FlywayReport> flywayReports = response.body()

        then:
        1 == flywayReports?.size()

        when:
        FlywayReport flywayReport = flywayReports[0]

        then:
        2 == flywayReport?.migrations?.size()
    }

    static class FlywayReport {
        List<Migration> migrations
    }

    static class Migration {

        String script

        void setId(String script) {
            this.script = script
        }
    }
}
