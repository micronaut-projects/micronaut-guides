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
package example.micronaut.controller

import example.micronaut.model.BookAvailability
import example.micronaut.model.BookInfo
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

@MicronautTest // <1>
class BooksControllerSpec extends Specification {

    @Inject
    @Client('${context-path}')
    HttpClient client // <2>

    void 'add book through client API'() {
        given:
        BookInfo body = new BookInfo('Building Microservices', BookAvailability.AVAILABLE)
        body.setAuthor('Sam Newman')
        body.setIsbn('9781492034025')

        when:
        def response = client.toBlocking()
                .exchange(HttpRequest.POST('/add', body)) // <3>

        then:
        response.status() == HttpStatus.OK // <4>
    }

    void 'search through client API'() {
        when:
        def response = client.toBlocking()
                .exchange(HttpRequest.GET(UriBuilder.of('/search')
                        .queryParam('book-name', 'Guide')
                        .build()
                ), Argument.listOf(BookInfo)) // <5>
        def body = response.body() // <6>

        then:
        response.status() == HttpStatus.OK
        body.size() == 2 // <7>
    }
}
