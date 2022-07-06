package example.micronaut;

import example.micronaut.domain.Genre;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.data.exceptions.DataAccessException;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;

@Controller("/genres")  // <2>
public class GenreController {

    protected final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) { // <3>
        this.genreRepository = genreRepository;
    }

    @Get("/{id}") // <4>
    @SingleResult
    public Mono<Genre> show(Long id) {
        return genreRepository
                .findById(id); // <5>
    }

    @Put // <6>
    public Mono<HttpResponse<?>> update(@Body @Valid GenreUpdateCommand command) { // <7>
        return genreRepository.update(command.getId(), command.getName())
                .map(e -> HttpResponse
                        .noContent()
                        .header(HttpHeaders.LOCATION, location(command.getId()).getPath())); // <8>

    }

    @Get("/list") // <9>
    public Mono<List<Genre>> list(@Valid Pageable pageable) { // <10>
        return genreRepository.findAll(pageable)
                .map(Page::getContent);
    }

    @Post // <11>
    public Mono<HttpResponse<?>> save(@Body("name") @NotBlank String name) {
        return genreRepository.save(name)
                .map(genre -> HttpResponse.created(genre)
                        .headers(headers -> headers.location(location(genre.getId()))));
    }

    @Post("/ex") // <12>
    public Mono<HttpResponse<?>> saveExceptions(@Body @NotBlank String name) {
        Mono<HttpResponse<?>> result =  genreRepository.save(name)
                .map(genre -> HttpResponse.created(genre)
                        .headers(headers -> headers.location(location(genre.getId()))));
        return result.onErrorReturn(throwable -> throwable instanceof DataAccessException, HttpResponse.noContent());
    }

    @Delete("/{id}") // <13>
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
