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
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import reactor.core.publisher.Mono

@Controller("/genres")  // <1>
class GenreController {

    protected final GenreRepository genreRepository

    GenreController(GenreRepository genreRepository) { // <2>
        this.genreRepository = genreRepository
    }

    @Get("/{id}") // <3>
    Mono<Genre> show(Long id) {
        return genreRepository
                .findById(id) // <4>
    }

    @Put // <5>
    Mono<HttpResponse<Genre>> update(@Body @Valid GenreUpdateCommand command) { // <6>
        return genreRepository.update(command.id, command.name)
                .map { HttpResponse
                        .noContent()
                        .header(HttpHeaders.LOCATION, location(command.id).path) } // <7>
    }

    @Get("/list") // <8>
    Mono<List<Genre>> list(@Valid Pageable pageable) { // <9>
        return genreRepository.findAll(pageable)
                .map(Page::getContent)
    }

    @Post // <10>
    Mono<HttpResponse<Genre>> save(@Body("name") @NotBlank String name) {
        return genreRepository.save(name)
                .map { Genre genre -> createdGenre(genre) }
    }

    @Post("/ex") // <11>
    Mono<MutableHttpResponse<Genre>> saveExceptions(@Body @NotBlank String name) {
        return genreRepository
                .saveWithException(name)
                .map { Genre genre -> createdGenre(genre) }
                .onErrorReturn(DataAccessException.class, HttpResponse.noContent())
    }

    @Delete("/{id}") // <12>
    Mono<HttpResponse<?>> delete(Long id) {
        return genreRepository.deleteById(id)
                .map { HttpResponse.noContent() }
    }

    private static MutableHttpResponse<Genre> createdGenre(Genre genre) {
        return HttpResponse.created(genre)
                .headers { headers -> headers.location(location(genre.id)) }
    }

    protected static URI location(Long id) {
        return URI.create('/genres/' + id)
    }

    protected static URI location(Genre genre) {
        return location(genre.id)
    }
}
