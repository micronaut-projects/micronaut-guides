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
import groovy.transform.CompileStatic
import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono

import jakarta.persistence.PersistenceException
import jakarta.validation.Valid

import static io.micronaut.http.HttpHeaders.LOCATION

@CompileStatic
@Controller('/genres')  // <1>
class GenreController {

    private final GenreRepository genreRepository

    GenreController(GenreRepository genreRepository) { // <2>
        this.genreRepository = genreRepository
    }

    @Get('/{id}') // <3>
    @SingleResult
    Publisher<Genre> show(Long id) {
        Mono.from(genreRepository.findById(id))
                .flatMap(genre -> genre.present ? Mono.just(genre.get()) : Mono.<Genre>empty()) // <4>
    }

    @Put // <5>
    Publisher<HttpResponse<?>> update(@Body @Valid GenreUpdateCommand command) { // <6>
        Mono.from(genreRepository.update(command.id, command.name))
                .map(ignored -> HttpResponse
                        .noContent()
                        .header(LOCATION, location(command.id).path)) // <7>
    }

    @Get(value = '/list{?args*}') // <8>
    Publisher<Genre> list(@Valid SortingAndOrderArguments args) {
        genreRepository.findAll(args)
    }

    @Post // <9>
    @SingleResult
    Publisher<MutableHttpResponse<Genre>> save(@Body @Valid GenreSaveCommand cmd) {
        Mono.from(genreRepository.save(cmd.name))
                .map(genre -> HttpResponse
                        .created(genre)
                        .headers(headers -> headers.location(location(genre.id))))
    }

    @Post('/ex') // <10>
    @SingleResult
    Publisher<MutableHttpResponse<Genre>> saveExceptions(@Body @Valid GenreSaveCommand cmd) {
        Mono.from(genreRepository.saveWithException(cmd.name))
                .map(genre -> HttpResponse
                        .created(genre)
                        .headers(headers -> headers.location(location(genre.id))))
                .onErrorReturn(PersistenceException, HttpResponse.noContent())
    }

    @Delete('/{id}') // <11>
    @Status(HttpStatus.NO_CONTENT) // <12>
    HttpResponse<?> delete(Long id) {
        genreRepository.deleteById(id)
        HttpResponse.noContent()
    }

    private URI location(Long id) {
        URI.create('/genres/' + id)
    }
}
