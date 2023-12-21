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
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject

@MicronautTest // <1>
class FruitDuplicationExceptionHandlerSpec extends BaseSpec {

    @Inject
    @Client("/")
    HttpClient httpClient // <2>

    void "duplicated fruit returns 422"() {
        when:
        FruitCommand banana = new FruitCommand("Banana")
        HttpRequest<?> request = HttpRequest.POST("/fruits", banana)
        HttpResponse<?> response = httpClient.toBlocking().exchange(request)

        then:
        HttpStatus.CREATED == response.status()

        when:
        httpClient.toBlocking().exchange(request)

        then:
        HttpClientResponseException e = thrown()
        HttpStatus.UNPROCESSABLE_ENTITY == e.status

        when:
        HttpRequest<?> deleteRequest = HttpRequest.DELETE("/fruits", banana)
        HttpResponse<?> deleteResponse = httpClient.toBlocking().exchange(deleteRequest)

        then:
        HttpStatus.NO_CONTENT == deleteResponse.status()
    }
}
