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

import io.micronaut.context.annotation.Primary
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest
class BookControllerSpec extends Specification {

    @Inject
    AnalyticsClient analyticsClient

    @Inject
    @Client('/')
    HttpClient client

    void 'test message is published to RabbitMQ when book found'() {
        when:
        Optional<Book> result = retrieveGet('/books/1491950358')

        then:
        result
        result.present
        1 * analyticsClient.updateAnalytics(_)
    }

    void 'test message is not published to RabbitMQ when book not found'() {
        when:
        retrieveGet '/books/INVALID'

        then:
        thrown HttpClientResponseException
        0 * analyticsClient.updateAnalytics(_)
    }

    @Primary
    @MockBean
    AnalyticsClient analyticsClient() {
        return Mock(AnalyticsClient)
    }

    private Optional<Book> retrieveGet(String url) {
        return client.toBlocking().retrieve(HttpRequest.GET(url), Argument.of(Optional, Book))
    }
}
