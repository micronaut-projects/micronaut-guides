/*
 * Copyright 2017-2023 original authors
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

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import static io.micronaut.http.HttpStatus.OK
import static io.micronaut.http.HttpStatus.UNAUTHORIZED
import static io.micronaut.http.MediaType.TEXT_PLAIN

@MicronautTest // <1>
class BasicAuthSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client // <2>

    void "by default every endpoint is secured"() {
        when: 'Accessing a secured URL without authenticating'
        client.toBlocking().exchange(HttpRequest.GET('/').accept(TEXT_PLAIN)) // <3>

        then: 'returns unauthorized'
        HttpClientResponseException e = thrown() // <4>
        e.status == UNAUTHORIZED
        e.response.headers.contains("WWW-Authenticate")
        e.response.headers.get("WWW-Authenticate") == 'Basic realm="Micronaut Guide"'
    }

    void "Verify HTTP Basic Auth works"() {
        when: 'A secured URL is accessed with Basic Auth'
        HttpRequest request = HttpRequest.GET('/')
                .basicAuth("sherlock", "password") // <5>
        HttpResponse<String> rsp = client.toBlocking().exchange(request, String) // <6>

        then: 'the endpoint can be accessed'
        rsp.status == OK
        rsp.body() == 'sherlock' // <7>
    }
}
