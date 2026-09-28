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

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Sql(value = ["classpath:schema.sql", "classpath:data.sql"])
@MicronautTest // <1>
class SecuritySpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <2>

    void "should not allow access to SaaS subscriptions they do not own"() {
        given:
        HttpRequest<?> request = HttpRequest.GET("/subscriptions/102")
                .basicAuth("sarah1", "abc123")

        when:
        httpClient.toBlocking().exchange(request, String)

        then:
        HttpClientResponseException thrown = thrown() // <3>
        thrown.status.code == HttpStatus.NOT_FOUND.code
    }

    void "should reject users who are not subscription owners"() {
        given:
        HttpRequest<?> badPasswordRequest = HttpRequest.GET("/subscriptions/99")
                .basicAuth("john-owns-no-subscriptions", "qrs456")

        when:
        httpClient.toBlocking().exchange(badPasswordRequest, String)

        then:
        HttpClientResponseException badPasswordEx = thrown() // <3>
        badPasswordEx.status.code == HttpStatus.FORBIDDEN.code
    }

    void "should not return a SaaS subscription with an unknown id"() {
        given:
        HttpRequest<?> request = HttpRequest.GET("/subscriptions/1000")
                .basicAuth("sarah1", "BAD-PASSWORD")

        when:
        httpClient.toBlocking().exchange(request, String)

        then:
        HttpClientResponseException ex = thrown() // <3>
        ex.status.code == HttpStatus.UNAUTHORIZED.code
    }
}
