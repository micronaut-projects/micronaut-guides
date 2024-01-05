/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.micronaut;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.micronaut.http.HttpStatus.OK;
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

        HttpResponse<List<FlywayReport>> response = client.exchange(
                HttpRequest.GET("/flyway"),
                Argument.listOf(FlywayReport.class));
        assertEquals(OK, response.status());

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
