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

import example.micronaut.domain.Genre
import groovy.transform.CompileStatic
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

@CompileStatic
@ExecuteOn(TaskExecutors.BLOCKING) // <1>
@Controller('/genres') // <2>
class GenreController {

    protected final GenreRepository genreRepository

    GenreController(GenreRepository genreRepository) { // <3>
        this.genreRepository = genreRepository
    }

    @Get('/{id}') // <4>
    Optional<Genre> show(Long id) {
        genreRepository
                .findById(id) // <5>
    }

    @Put // <6>
    HttpResponse update(@Body @Valid GenreUpdateCommand command) { // <7>
        genreRepository.update(command.id, command.name)

        HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.id).path) // <8>
    }

    @Get('/list') // <9>
    List<Genre> list(@Valid Pageable pageable) { // <10>
        genreRepository.findAll(pageable).content
    }

    @Post // <11>
    HttpResponse<Genre> save(@Body('name') @NotBlank String name) {
        Genre genre = genreRepository.save(name)

        HttpResponse.created(genre)
                .headers(headers -> headers.location(location(genre)))
    }

    @Post('/ex') // <12>
    HttpResponse<Genre> saveExceptions(@Body @NotBlank String name) {
        try {
            Genre genre = genreRepository.saveWithException(name)
            HttpResponse.created(genre)
                    .headers(headers -> headers.location(location(genre)))
        } catch(DataAccessException ex) {
            return HttpResponse.noContent()
        }
    }

    @Delete('/{id}') // <13>
    @Status(HttpStatus.NO_CONTENT)
    void delete(Long id) {
        genreRepository.deleteById(id)
    }

    private static URI location(Long id) { URI.create("/genres/$id") }

    private static URI location(Genre genre) { location(genre.id) }
}

