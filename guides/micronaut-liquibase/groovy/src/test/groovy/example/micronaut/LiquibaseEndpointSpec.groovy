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
