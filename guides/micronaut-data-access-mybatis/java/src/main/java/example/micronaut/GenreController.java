package example.micronaut;

import example.micronaut.domain.Genre;
import example.micronaut.genre.GenreRepository;
import example.micronaut.genre.GenreSaveCommand;
import example.micronaut.genre.GenreUpdateCommand;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Controller("/genres") // <1>
public class GenreController {

    protected final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) { // <2>
        this.genreRepository = genreRepository;
    }

    @Get("/{id}") // <3>
    public Genre show(Long id) {
        return genreRepository
                .findById(id)
                .orElse(null); // <4>
    }

    @Put // <5>
    public HttpResponse<?> update(@Body @Valid GenreUpdateCommand command) { // <6>
        genreRepository.update(command.getId(), command.getName());
        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getId()).getPath()); // <7>
    }

    @Get(value = "/list{?args*}") // <8>
    public List<Genre> list(@Valid ListingArguments args) {
        return genreRepository.findAll(args);
    }

    @Post // <9>
    public HttpResponse<Genre> save(@Body @Valid GenreSaveCommand cmd) {
        Genre genre = genreRepository.save(cmd.getName());

        return HttpResponse
                .created(genre)
                .headers(headers -> headers.location(location(genre.getId())));
    }

    @Delete("/{id}") // <10>
    public HttpResponse<?> delete(Long id) {
        genreRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    protected URI location(Long id) {
        return URI.create("/genres/" + id);
    }

    protected URI location(Genre genre) {
        return location(genre.getId());
    }
}
