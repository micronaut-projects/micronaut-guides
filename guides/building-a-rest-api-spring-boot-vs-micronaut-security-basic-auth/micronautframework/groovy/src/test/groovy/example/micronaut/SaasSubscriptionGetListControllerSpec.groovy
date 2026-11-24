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
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import net.minidev.json.JSONArray
import spock.lang.Specification

@Sql(value = ["classpath:schema.sql", "classpath:data.sql"]) // <1>
@MicronautTest // <2>
class SaasSubscriptionGetListControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <3>

    void "should return a sorted page of SaaS subscriptions"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()
        URI uri = UriBuilder.of("/subscriptions")
                .queryParam("page", 0)
                .queryParam("size", 1)
                .queryParam("sort", "cents,desc")
                .build()
        HttpRequest<?> request = HttpRequest.GET(uri).basicAuth("sarah1", "abc123")

        when:
        HttpResponse<String> response = client.exchange(request, String)

        then:
        response.status.code == HttpStatus.OK.code

        when:
        DocumentContext documentContext = JsonPath.parse(response.body())
        JSONArray page = documentContext.read('$[*]')
        Integer cents = documentContext.read('$[0].cents')

        then:
        page.size() == 1
        cents == 4900
    }

    void "should return a page of SaaS subscriptions"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()
        URI uri = UriBuilder.of("/subscriptions")
                .queryParam("page", 0)
                .queryParam("size", 1)
                .build()
        HttpRequest<?> request = HttpRequest.GET(uri).basicAuth("sarah1", "abc123")

        when:
        HttpResponse<String> response = client.exchange(request, String)

        then:
        response.status.code == HttpStatus.OK.code

        when:
        DocumentContext documentContext = JsonPath.parse(response.body())
        JSONArray page = documentContext.read('$[*]')

        then:
        page.size() == 1
    }

    void "should return all SaaS subscriptions when list is requested"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()
        HttpRequest<?> request = HttpRequest.GET("/subscriptions").basicAuth("sarah1", "abc123")

        when:
        HttpResponse<String> response = client.exchange(request, String)

        then:
        response.status.code == HttpStatus.OK.code

        when:
        DocumentContext documentContext = JsonPath.parse(response.body())
        int saasSubscriptionCount = documentContext.read('$.length()')
        JSONArray ids = documentContext.read('$..id')
        JSONArray cents = documentContext.read('$..cents')

        then:
        saasSubscriptionCount == 3
        ids.containsAll([99, 100, 101])
        cents.containsAll([1400, 2900, 4900])
    }
}
