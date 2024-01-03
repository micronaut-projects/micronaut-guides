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

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest
class GraphQLControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient client

    void 'test graphQL controller'() {
        given:
        String query = '{ "query": "{ bookById(id:\\"book-1\\") { name, pageCount, author { firstName, lastName} } }" }'

        when:
        HttpRequest request = HttpRequest.POST('/graphql', query)
        HttpResponse<Map> rsp = client.toBlocking().exchange(request, Argument.of(Map))

        then:
        rsp.status() == HttpStatus.OK
        rsp.body()

        and:
        Map bookInfo = rsp.getBody(Map).get()
        bookInfo.data.bookById
        bookInfo.data.bookById.name == "Harry Potter and the Philosopher's Stone"
        bookInfo.data.bookById.pageCount == 223
        bookInfo.data.bookById.author
        bookInfo.data.bookById.author.firstName == 'Joanne'
        bookInfo.data.bookById.author.lastName == 'Rowling'
    }
}
