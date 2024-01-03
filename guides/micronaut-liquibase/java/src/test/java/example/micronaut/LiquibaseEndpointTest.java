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
import io.micronaut.serde.annotation.Serdeable;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.micronaut.http.HttpStatus.OK;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest // <1>
public class LiquibaseEndpointTest {

    @Inject
    @Client("/")  // <2>
    HttpClient httpClient;

    @Test
    void migrationsAreExposedViaAndEndpoint() {
        BlockingHttpClient client = httpClient.toBlocking();

        HttpResponse<List<LiquibaseReport>> response = client.exchange(
                HttpRequest.GET("/liquibase"),
                Argument.listOf(LiquibaseReport.class)
        );
        assertEquals(OK, response.status());

        LiquibaseReport liquibaseReport = response.body().get(0);
        assertNotNull(liquibaseReport);
        assertNotNull(liquibaseReport.getChangeSets());
        assertEquals(2, liquibaseReport.getChangeSets().size());
    }

    @Serdeable
    static class LiquibaseReport {

        private List<ChangeSet> changeSets;

        public void setChangeSets(List<ChangeSet> changeSets) {
            this.changeSets = changeSets;
        }

        public List<ChangeSet> getChangeSets() {
            return changeSets;
        }
    }

    @Serdeable
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
