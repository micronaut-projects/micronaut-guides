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
package example.micronaut;

import example.micronaut.domain.Genre;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Status;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import jakarta.persistence.PersistenceException;
import jakarta.validation.Valid;
import java.net.URI;

import static io.micronaut.http.HttpHeaders.LOCATION;

@Controller("/genres")  // <1>
class GenreController {

    private final GenreRepository genreRepository;

    GenreController(GenreRepository genreRepository) { // <2>
        this.genreRepository = genreRepository;
    }

    @Get("/{id}") // <3>
    @SingleResult
    Publisher<Genre> show(Long id) {
        return Mono.from(genreRepository.findById(id))
                .flatMap(g -> g.map(Mono::just).orElseGet(Mono::empty)); // <4>
    }

    @Put // <5>
    Publisher<HttpResponse<?>> update(@Body @Valid GenreUpdateCommand command) { // <6>
        return Mono.from(genreRepository.update(command.getId(), command.getName()))
                .map(i -> HttpResponse
                        .noContent()
                        .header(LOCATION, location(command.getId()).getPath())); // <7>
    }

    @Get(value = "/list{?args*}") // <8>
    Publisher<Genre> list(@Valid SortingAndOrderArguments args) {
        return genreRepository.findAll(args);
    }

    @Post // <9>
    @SingleResult
    Publisher<HttpResponse<Genre>> save(@Body @Valid GenreSaveCommand cmd) {
        return Mono.from(genreRepository.save(cmd.getName()))
                .map(genre -> HttpResponse
                        .created(genre)
                        .headers(headers -> headers.location(location(genre.getId())))
                );
    }

    @Post("/ex") // <10>
    @SingleResult
    Publisher<MutableHttpResponse<Genre>> saveExceptions(@Body @Valid GenreSaveCommand cmd) {
        return Mono.from(genreRepository.saveWithException(cmd.getName()))
                .map(genre -> HttpResponse
                        .created(genre)
                        .headers(headers -> headers.location(location(genre.getId()))))
                .onErrorReturn(PersistenceException.class, HttpResponse.noContent());
    }

    @Delete("/{id}") // <11>
    @Status(HttpStatus.NO_CONTENT) // <12>
    <T> HttpResponse<T> delete(Long id) {
        genreRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    private URI location(Long id) {
        return URI.create("/genres/" + id);
    }
}
