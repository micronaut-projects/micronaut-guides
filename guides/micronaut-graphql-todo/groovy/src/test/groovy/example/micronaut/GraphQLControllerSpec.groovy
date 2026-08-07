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

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification

import jakarta.inject.Inject

@MicronautTest // <1>
class GraphQLControllerSpec extends Specification {

    @Inject
    @Client('/')
    HttpClient client // <2>

    void 'test GraphQL controller'() {
        when:
        List<Map> todos = getTodos()

        then:
        todos.empty

        when:
        Long id = createToDo('Test GraphQL', 'Tim Yates')

        then:
        id == 1

        when:
        todos = getTodos()

        then:
        todos.size() == 1
        todos[0].title == 'Test GraphQL'
        !Boolean.parseBoolean(todos[0].completed.toString())
        todos[0].author.username == 'Tim Yates'

        when:
        Boolean completed = markAsCompleted(id)

        then:
        completed

        when:
        todos = getTodos()

        then:
        todos.size() == 1
        todos[0].title == 'Test GraphQL'
        Boolean.parseBoolean(todos[0].completed.toString())
        todos[0].author.username == 'Tim Yates'
    }

    private HttpResponse<Map> fetch(String query) {
        HttpRequest<String> request = HttpRequest.POST('/graphql', query)
        HttpResponse<Map> response = client.toBlocking().exchange(request, Argument.of(Map))
        assert response.status() == HttpStatus.OK
        assert response.body()
        response
    }

    private List<Map> getTodos() {
        String query = '{"query":"query { toDos { title, completed, author { id, username } } }"}'
        HttpResponse<Map> response = fetch(query)
        ((Map) response.body().data).toDos as List<Map>
    }

    private Long createToDo(String title, String author) {
        String query = """{"query": "mutation { createToDo(title: \\"$title\\", author: \\"$author\\") { id } }" }"""
        HttpResponse<Map> response = fetch(query)
        ((Map) ((Map) response.body().data).createToDo).id.toString().toLong()
    }

    private Boolean markAsCompleted(Long id) {
        String query = """{"query": "mutation { completeToDo(id: \\"$id\\") }" }"""
        HttpResponse<Map> response = fetch(query)
        ((Map) response.body().data).completeToDo as Boolean
    }
}
