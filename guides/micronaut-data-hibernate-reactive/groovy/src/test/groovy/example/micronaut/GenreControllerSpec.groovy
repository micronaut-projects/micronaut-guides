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

import example.micronaut.domain.Genre
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import spock.lang.Specification

import java.util.concurrent.Callable

import static org.awaitility.Awaitility.await
import static org.hamcrest.Matchers.equalTo

@MicronautTest(transactional = false) // <1>
class GenreControllerSpec extends Specification {

    @Inject
    @Client("/")
    HttpClient httpClient // <2>

    void "find non-existing genre returns 404"() {
        when:
        httpClient.toBlocking().exchange(HttpRequest.GET('/genres/99'))

        then:
        HttpClientResponseException thrown = thrown()
        thrown.response
        thrown.status == HttpStatus.NOT_FOUND
    }

    void "genre CRUD operations"() {
        given:
        List<Long> genreIds = []

        when:
        HttpRequest<?> request = HttpRequest.POST('/genres', [name: 'DevOps']) // <3>
        HttpResponse<?> response = httpClient.toBlocking().exchange(request)
        genreIds << entityId(response)

        then:
        response.status == HttpStatus.CREATED

        when:
        request = HttpRequest.POST('/genres', [name: 'Microservices']) // <3>
        response = httpClient.toBlocking().exchange(request)

        then:
        response.status == HttpStatus.CREATED

        when:
        Long id = entityId(response)
        genreIds << id
        request = HttpRequest.GET('/genres/' + id)
        Genre genre = httpClient.toBlocking().retrieve(request, Genre) // <4>

        then:
        genre.name == 'Microservices'

        when:
        request = HttpRequest.PUT('/genres', new GenreUpdateCommand(id, 'Micro-services'))
        response = httpClient.toBlocking().exchange(request) // <5>

        then:
        response.status == HttpStatus.NO_CONTENT

        when:
        request = HttpRequest.GET('/genres/' + id)
        genre = httpClient.toBlocking().retrieve(request, Genre)

        then:
        genre.name == 'Micro-services'

        when:
        await().until(countGenres(), equalTo(2))

        then:
        noExceptionThrown()

        when:
        request = HttpRequest.POST('/genres/ex', [name: 'Microservices']) // <3>
        response = httpClient.toBlocking().exchange(request)

        then:
        response.status == HttpStatus.NO_CONTENT

        when:
        await().until(countGenres(), equalTo(2))

        then:
        noExceptionThrown()

        when:
        request = HttpRequest.GET('/genres/list?size=1')
        List<Genre> genres = httpClient.toBlocking().retrieve(request, Argument.listOf(Genre))

        then:
        genres.size() == 1
        genres[0].name == 'DevOps'

        when:
        request = HttpRequest.GET('/genres/list?size=1&sort=name,desc')
        genres = httpClient.toBlocking().retrieve(request, Argument.listOf(Genre))

        then:
        genres.size() == 1
        genres[0].name == 'Micro-services'

        when:
        request = HttpRequest.GET('/genres/list?size=1&page=2')
        genres = httpClient.toBlocking().retrieve(request, Argument.listOf(Genre))

        then:
        genres.size() == 0

        when:
        genreIds.each { Long genreId ->
            request = HttpRequest.DELETE('/genres/' + genreId)
            response = httpClient.toBlocking().exchange(request)
            assert response.status == HttpStatus.NO_CONTENT
        }

        then:
        noExceptionThrown()
    }

    private Callable<Integer> countGenres() {
        return {
            httpClient
                    .toBlocking()
                    .retrieve(HttpRequest.GET('/genres/list'), Argument.listOf(Genre))
                    .size()
        } as Callable<Integer>
    }

    protected Long entityId(HttpResponse<?> response) {
        String path = '/genres/'
        String value = response.header(HttpHeaders.LOCATION)
        if (value == null) {
            return null
        }
        int index = value.indexOf(path)
        if (index != -1) {
            return Long.valueOf(value.substring(index + path.length()))
        }
        return null
    }
}
