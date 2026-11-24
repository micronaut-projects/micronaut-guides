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

import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Sql(value = ["classpath:schema.sql", "classpath:data.sql"]) // <1>
@MicronautTest // <2>
class SaasSubscriptionControllerGetSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <3>

    void "should return a SaaS subscription when data is saved"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()
        HttpRequest<?> request = HttpRequest.GET("/subscriptions/99").basicAuth("sarah1", "abc123")

        when:
        HttpResponse<String> response = client.exchange(request, String)

        then:
        response.status.code == HttpStatus.OK.code

        when:
        DocumentContext documentContext = JsonPath.parse(response.body())
        Number id = documentContext.read('$.id')
        String name = documentContext.read('$.name')
        Integer cents = documentContext.read('$.cents')

        then:
        id != null
        id == 99
        name != null
        name == "Advanced"
        cents == 2900
    }

    void "should not return a SaaS subscription with an unknown id"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()
        HttpRequest<?> request = HttpRequest.GET("/subscriptions/1000").basicAuth("sarah1", "abc123")

        when:
        client.exchange(request, String)

        then:
        HttpClientResponseException thrown = thrown()
        thrown.status.code == HttpStatus.NOT_FOUND.code
        thrown.response.body.empty
    }
}
