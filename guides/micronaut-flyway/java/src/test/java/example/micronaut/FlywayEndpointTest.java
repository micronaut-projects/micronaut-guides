package example.micronaut;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest // <1>
public class FlywayEndpointTest {
    @Inject
    @Client("/")  // <2>
    HttpClient httpClient;

    @Test
    void migrationsAreExposedViaAndEndpoint() {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<List<FlywayReport>> response = client.exchange(HttpRequest.GET("/flyway"), Argument.listOf(FlywayReport.class));
        assertEquals(HttpStatus.OK, response.status());
        List<FlywayReport> flywayReports = response.body();
        assertNotNull(flywayReports);
        assertEquals(1, flywayReports.size());
        FlywayReport flywayReport = flywayReports.get(0);
        assertNotNull(flywayReport);
        assertNotNull(flywayReport.getMigrations());
        assertEquals(2, flywayReport.getMigrations().size());
    }

    static class FlywayReport {
        private List<Migration> migrations;

        public void setMigrations(List<Migration> migrations) {
            this.migrations = migrations;
        }

        public List<Migration> getMigrations() {
            return migrations;
        }
    }

    static class Migration {
        private String script;

        public void setId(String script) {
            this.script = script;
        }

        public String getScript() {
            return script;
        }
    }
}
