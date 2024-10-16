package example.micronaut;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@MicronautTest
public class MetricsTest {

    @Client("/")
    @Inject
    HttpClient httpClient;

    @Test
    void metricsEndpointsIsExposed() {
        HttpResponse<Metrics> response = httpClient.toBlocking().exchange("/metrics", Metrics.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatus());
        Metrics metrics = response.body();
        Assertions.assertNotNull(metrics);
        Assertions.assertNotNull(metrics.getNames());
        Assertions.assertTrue(metrics.getNames().contains("process.cpu.usage"));
    }

    @Introspected
    static class Metrics {
        @NonNull
        @NotNull
        @NotEmpty
        private List<String> names;

        public Metrics(@NonNull List<String> names) {
            this.names = names;
        }

        @NonNull
        public List<String> getNames() {
            return names;
        }

        public void setNames(@NonNull List<String> names) {
            this.names = names;
        }
    }
}
