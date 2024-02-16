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
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@ExecuteOn(TaskExecutors.BLOCKING)  // <1>
@Controller("/genres")  // <2>
public class GenreController {

    protected final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) { // <3>
        this.genreRepository = genreRepository;
    }

    @Get("/{id}") // <4>
    public Optional<Genre> show(Long id) {
        return genreRepository
                .findById(id); // <5>
    }

    @Put // <6>
    public HttpResponse update(@Body @Valid GenreUpdateCommand command) { // <7>
        genreRepository.update(command.getId(), command.getName());
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getId()).getPath()); // <8>
    }

    @Get("/list") // <9>
    public List<Genre> list(@Valid Pageable pageable) { // <10>
        return genreRepository.findAll(pageable).getContent();
    }

    @Post // <11>
    public HttpResponse<Genre> save(@Body("name") @NotBlank String name) {
        Genre genre = genreRepository.save(name);

        return HttpResponse
                .created(genre)
                .headers(headers -> headers.location(location(genre.getId())));
    }

    @Post("/ex") // <12>
    public HttpResponse<Genre> saveExceptions(@Body @NotBlank String name) {
        try {
            Genre genre = genreRepository.saveWithException(name);
            return HttpResponse
                    .created(genre)
                    .headers(headers -> headers.location(location(genre.getId())));
        } catch(DataAccessException e) {
            return HttpResponse.noContent();
        }
    }

    @Delete("/{id}") // <13>
    @Status(HttpStatus.NO_CONTENT)
    public void delete(Long id) {
        genreRepository.deleteById(id);
    }

    protected URI location(Long id) {
        return URI.create("/genres/" + id);
    }

    protected URI location(Genre genre) {
        return location(genre.getId());
    }
}
