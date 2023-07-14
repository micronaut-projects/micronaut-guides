package example.micronaut;

import example.micronaut.domain.Genre;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;

@Controller("/genres")  // <1>
public class GenreController {

    protected final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) { // <2>
        this.genreRepository = genreRepository;
    }

    @Get("/{id}") // <3>
    public Mono<Genre> show(Long id) {
        return genreRepository
                .findById(id); // <4>
    }

    @Put // <5>
    public Mono<HttpResponse<Genre>> update(@Body @Valid GenreUpdateCommand command) { // <6>
        return genreRepository.update(command.getId(), command.getName())
                .map(e -> HttpResponse
                        .<Genre>noContent()
                        .header(HttpHeaders.LOCATION, location(command.getId()).getPath())); // <7>

    }

    @Get("/list") // <8>
    public Mono<List<Genre>> list(@Valid Pageable pageable) { // <9>
        return genreRepository.findAll(pageable)
                .map(Page::getContent);
    }

    @Post // <10>
    public Mono<HttpResponse<Genre>> save(@Body("name") @NotBlank String name) {
        return genreRepository.save(name)
                .map(genre -> HttpResponse.created(genre)
                        .headers(headers -> headers.location(location(genre.getId()))));
    }

    @Post("/ex") // <11>
    public Mono<MutableHttpResponse<Genre>> saveExceptions(@Body @NotBlank String name) {
        return genreRepository
                .saveWithException(name)
                .map(genre -> HttpResponse
                        .created(genre)
                        .headers(headers -> headers.location(location(genre.getId())))
                )
                .onErrorReturn(DataAccessException.class, HttpResponse.noContent());
    }

    @Delete("/{id}") // <12>
    public Mono<HttpResponse<?>> delete(Long id) {
        return genreRepository.deleteById(id)
                .map(deleteId -> HttpResponse.noContent());
    }

    protected URI location(Long id) {
        return URI.create("/genres/" + id);
    }

    protected URI location(Genre genre) {
        return location(genre.getId());
    }
}
