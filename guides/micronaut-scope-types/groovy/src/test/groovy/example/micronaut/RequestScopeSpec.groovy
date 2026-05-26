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

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest // <1>
class RequestScopeSpec extends Specification {
    @Inject
    @Client('/')
    HttpClient httpClient // <2>
// end::imports[]
// tag::test[]

    void 'request scope creates an instance associated with each HTTP request'() {
        given:
        String path = '/request'
        Set<String> responses = executeRequest(httpClient, createRequest(path)) as Set

        expect:
        responses.size() == 1 // <3>

        when:
        responses.addAll(executeRequest(httpClient, createRequest(path)))

        then:
        responses.size() == 2 // <4>
    }

    private static List<String> executeRequest(HttpClient client, HttpRequest<?> request) {
        client.toBlocking().retrieve(request, Argument.listOf(String))
    }

    private static HttpRequest<?> createRequest(String path) {
        HttpRequest.GET(path).header('UUID', UUID.randomUUID().toString())
    }
}
//end::test[]
