package example.micronaut;

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

@MicronautTest
public class LiquibaseEndpointTest {
    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void migrationsAreExposedViaAndEndpoint() {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<LiquibaseReport> response = client.exchange(HttpRequest.GET("/liquibase"), LiquibaseReport.class);
        assertEquals(HttpStatus.OK, response.status());
        LiquibaseReport liquibaseReport = response.body();
        assertNotNull(liquibaseReport);
        assertNotNull(liquibaseReport.getChangeSets());
        assertEquals(2, liquibaseReport.getChangeSets().size());
    }

    static class LiquibaseReport {
        private List<ChangeSet> changeSets;

        public void setChangeSets(List<ChangeSet> changeSets) {
            this.changeSets = changeSets;
        }

        public List<ChangeSet> getChangeSets() {
            return changeSets;
        }
    }

    static class ChangeSet {
        private String id;

        public void setId(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
}
