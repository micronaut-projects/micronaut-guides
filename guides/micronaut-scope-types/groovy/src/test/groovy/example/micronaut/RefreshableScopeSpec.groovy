/*
 * Copyright 2017-2026 original authors
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
/*
//tag::package[]
package example.micronaut
//end::package[]
*/
//tag::imports[]

import io.micronaut.context.annotation.Property
import io.micronaut.core.type.Argument
import io.micronaut.core.util.StringUtils
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Property(name = 'endpoints.refresh.enabled', value = StringUtils.TRUE) // <1>
@Property(name = 'endpoints.refresh.sensitive', value = StringUtils.FALSE) // <2>
@MicronautTest // <3>
class RefreshableScopeSpec extends Specification {
    @Inject
    @Client('/')
    HttpClient httpClient // <4>
//end::imports[]
    //tag::test[]

    void 'refreshable scope allows a bean state to be refreshed via the refresh endpoint'() {
        given:
        String path = '/refreshable'
        Set<String> responses = executeRequest(httpClient, path) as Set

        expect:
        responses.size() == 1 // <5>

        when:
        responses.addAll(executeRequest(httpClient, path))

        then:
        responses.size() == 1 // <6>

        when:
        refresh(httpClient) // <7>
        responses.addAll(executeRequest(httpClient, path))

        then:
        responses.size() == 2 // <8>
    }

    private static void refresh(HttpClient client) {
        client.toBlocking().exchange(HttpRequest.POST('/refresh', [force: true]))
    }

    private static List<String> executeRequest(HttpClient client, String path) {
        client.toBlocking().retrieve(HttpRequest.GET(path), Argument.listOf(String))
    }
}
//end::test[]
