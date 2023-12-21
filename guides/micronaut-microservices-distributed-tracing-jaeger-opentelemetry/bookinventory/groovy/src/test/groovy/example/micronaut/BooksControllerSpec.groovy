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
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest
class BooksControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    void "for a book with inventory true is returned"() {
        when:
        Boolean hasStock = client.toBlocking().retrieve(HttpRequest.GET("/books/stock/1491950358"), Boolean)

        then:
        noExceptionThrown()
        hasStock
    }

    void "for a book without inventory false is returned"() {
        when:
        Boolean hasStock = client.toBlocking().retrieve(HttpRequest.GET("/books/stock/1680502395"), Boolean)

        then:
        noExceptionThrown()
        hasStock == Boolean.FALSE
    }

    void "for an invalid ISBN 404 is returned"() {
        when:
        client.toBlocking().retrieve(HttpRequest.GET("/books/stock/XXXXX"))

        then:
        def e = thrown(HttpClientResponseException)
        e.response.status == HttpStatus.NOT_FOUND
    }
}
