package example.micronaut;

import example.micronaut.domain.Genre;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.data.exceptions.DataAccessException;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@ExecuteOn(TaskExecutors.IO)  // <1>
@Controller("/genres")  // <2>
public class GenreController {

    protected final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) { // <3>
        this.genreRepository = genreRepository;
    }

    @Get("/{id}") // <4>
    public Genre show(Long id) {
        return genreRepository
                .findById(id)
                .orElse(null); // <5>
    }

    @Put // <6>
    public HttpResponse update(@Body @Valid GenreUpdateCommand command) { // <7>
        int numberOfEntitiesUpdated = genreRepository.update(command.getId(), command.getName());

        return HttpResponse
                .noContent()
                .header(HttpHeaders.LOCATION, location(command.getId()).getPath()); // <8>
    }

    @Get(value = "/list{?args*}") // <9>
    public List<Genre> list(@Valid SortingAndOrderArguments args) {
        Optional<Sort> sortOptional = parseSort(args);
        if (args.getMax().isPresent()) {
            int size = args.getMax().get();
            int page = args.getOffset().isPresent() ? (args.getOffset().get() / size) : 0;
            Pageable pageable = sortOptional
                    .map(sort -> Pageable.from(page, size, sort))
                    .orElseGet(() -> Pageable.from(page, size));
            return genreRepository.findAll(pageable).getContent();
        }
        Iterable<Genre> genreIterable = sortOptional.map(genreRepository::findAll)
                .orElseGet(genreRepository::findAll);
        return StreamSupport.stream(genreIterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    private Optional<Sort> parseSort(SortingAndOrderArguments args) {
        if (args.getSort().isPresent() && args.getOrder().isPresent()) {
            return Optional.of(Sort.of(args.getOrder().get().equals("desc") ?
                    Sort.Order.desc(args.getSort().get()) :
                    Sort.Order.asc(args.getSort().get())));

        }
        return Optional.empty();
    }

    @Post // <10>
    public HttpResponse<Genre> save(@Body @Valid GenreSaveCommand cmd) {
        Genre genre = genreRepository.save(cmd.getName());

        return HttpResponse
                .created(genre)
                .headers(headers -> headers.location(location(genre.getId())));
    }

    @Post("/ex") // <11>
    public HttpResponse<Genre> saveExceptions(@Body @Valid GenreSaveCommand cmd) {
        try {
            Genre genre = genreRepository.saveWithException(cmd.getName());
            return HttpResponse
                    .created(genre)
                    .headers(headers -> headers.location(location(genre.getId())));
        } catch(DataAccessException e) {
            return HttpResponse.noContent();
        }
    }

    @Delete("/{id}") // <12>
    public HttpResponse delete(Long id) {
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
