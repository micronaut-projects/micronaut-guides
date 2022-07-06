package example.micronaut;

import example.micronaut.domain.Genre;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static io.micronaut.http.HttpHeaders.LOCATION;

@ExecuteOn(TaskExecutors.IO)  // <1>
@Controller("/genres")  // <2>
class GenreController {

    private final GenreRepository genreRepository;

    GenreController(GenreRepository genreRepository) { // <3>
        this.genreRepository = genreRepository;
    }

    @Get("/{id}")
        // <4>
    Publisher<Genre> show(Long id) {
        return genreRepository
                .findById(id); // <5>
    }

    @Put
        // <6>
    HttpResponse<?> update(@Body @Valid GenreUpdateCommand command) { // <7>
        genreRepository.update(command.getId(), command.getName());

        return HttpResponse
                .noContent()
                .header(LOCATION, location(command.getId()).getPath()); // <8>
    }

    @Get(value = "/list{?args*}")
        // <9>
    Publisher<Genre> list(@Valid SortingAndOrderArguments args) {
        return genreRepository.findAll(args);
    }

    @Post
        // <10>
    Publisher<HttpResponse<Genre>> save(@Body @Valid GenreSaveCommand cmd) {
        return Mono.from(genreRepository.save(cmd.getName())).map(genre -> HttpResponse
                .created(genre)
                .headers(headers -> headers.location(location(genre.getId()))));
    }

    @Post("/ex")
        // <11>
    Publisher<MutableHttpResponse<Genre>> saveExceptions(@Body @Valid GenreSaveCommand cmd) {
            return Mono.from(genreRepository.saveWithException(cmd.getName()))
                    .map(genre -> HttpResponse
                            .created(genre)
                            .headers(headers -> headers.location(location(genre.getId()))))
                    .onErrorReturn(PersistenceException.class, HttpResponse.noContent());
    }

    @Delete("/{id}")
        // <12>
    HttpResponse<?> delete(Long id) {
        genreRepository.deleteById(id);
        return HttpResponse.noContent();
    }

    private URI location(Long id) {
        return URI.create("/genres/" + id);
    }
}
