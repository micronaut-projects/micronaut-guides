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
        when:
        def body = makeRequest("book-1")

        then:
        body.data.bookById.name == "Harry Potter and the Philosopher's Stone"
        body.data.bookById.pageCount == 223
        body.data.bookById.author.firstName == 'Joanne'
        body.data.bookById.author.lastName == 'Rowling'
    }

    void 'test graphQL controller empty response'() {
        when:
        def body = makeRequest("missing-id")

        then:
        body.data.containsKey("bookById")
        body.data.bookById == null
    }

    private Map<String, Object> makeRequest(String id) {
        String query = """{ "query": "{ bookById(id:\\"$id\\") { name, pageCount, author { firstName, lastName} } }" }"""
        HttpRequest<String> request = HttpRequest.POST('/graphql', query)
        HttpResponse<Map<String, Object>> rsp = client.toBlocking().exchange(request, Argument.mapOf(String, Object))

        assert rsp.status() == HttpStatus.OK
        assert rsp.body()
        rsp.body()
    }
}
