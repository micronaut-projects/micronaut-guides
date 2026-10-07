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
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import jakarta.persistence.PersistenceException
import jakarta.validation.Valid
import java.net.URI

@ExecuteOn(TaskExecutors.BLOCKING) // <1>
@Controller("/genres") // <2>
open class GenreController(private val genreRepository: GenreRepository) { // <3>

    @Get("/{id}") // <4>
    fun show(id: Long): Genre? {
        return genreRepository.findById(id) // <5>
    }

    @Put // <6>
    open fun update(@Valid @Body command: GenreUpdateCommand): HttpResponse<*> { // <7>
        genreRepository.update(command.id, command.name)

        return HttpResponse
            .noContent<Any>()
            .header(HttpHeaders.LOCATION, location(command.id).path) // <8>
    }

    @Get(value = "/list{?args*}") // <9>
    open fun list(@Valid args: SortingAndOrderArguments): List<Genre> {
        return genreRepository.findAll(args)
    }

    @Post // <10>
    open fun save(@Valid @Body cmd: GenreSaveCommand): HttpResponse<Genre> {
        val genre = genreRepository.save(cmd.name)

        return HttpResponse
            .created(genre)
            .headers { headers -> headers.location(location(genre.id!!)) }
    }

    @Post("/ex") // <11>
    open fun saveExceptions(@Valid @Body cmd: GenreSaveCommand): HttpResponse<Genre> {
        return try {
            val genre = genreRepository.saveWithException(cmd.name)
            HttpResponse
                .created(genre)
                .headers { headers -> headers.location(location(genre.id!!)) }
        } catch (e: PersistenceException) {
            HttpResponse.noContent()
        }
    }

    @Delete("/{id}") // <12>
    open fun delete(id: Long): HttpResponse<*> {
        genreRepository.deleteById(id)
        return HttpResponse.noContent<Any>()
    }

    private fun location(id: Long): URI {
        return URI.create("/genres/$id")
    }
}
