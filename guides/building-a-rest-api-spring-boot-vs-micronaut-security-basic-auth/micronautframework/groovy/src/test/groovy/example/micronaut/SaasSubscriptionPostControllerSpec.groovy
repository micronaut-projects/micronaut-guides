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
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.BlockingHttpClient
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.annotation.Sql
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@Sql(value = ["classpath:schema.sql", "classpath:data.sql"])
@MicronautTest // <1>
class SaasSubscriptionPostControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <2>

    void "should create a new SaaS subscription"() {
        given:
        BlockingHttpClient client = httpClient.toBlocking()
        SaasSubscriptionSave subscription = new SaasSubscriptionSave("Advanced", 2900)
        HttpRequest<?> request = HttpRequest.POST("/subscriptions", subscription)
                .basicAuth("sarah1", "abc123")

        when:
        HttpResponse<Void> createResponse = client.exchange(request, Void)

        then:
        createResponse.status.code == HttpStatus.CREATED.code
        createResponse.headers.get(HttpHeaders.LOCATION, URI).present

        when:
        URI locationOfNewSaasSubscription = createResponse.headers.get(HttpHeaders.LOCATION, URI).get()
        request = HttpRequest.GET(locationOfNewSaasSubscription).basicAuth("sarah1", "abc123")
        HttpResponse<String> getResponse = client.exchange(request, String)
        DocumentContext documentContext = JsonPath.parse(getResponse.body())
        Number id = documentContext.read('$.id')
        Integer cents = documentContext.read('$.cents')

        then:
        getResponse.status.code == HttpStatus.OK.code
        id != null
        cents == 2900
    }
}
