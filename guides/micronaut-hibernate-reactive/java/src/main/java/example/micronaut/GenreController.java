package example.micronaut;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import org.reactivestreams.Publisher;

@Controller
public class GenreController {

    private final GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Get
    Publisher<Genre> list() {
        return genreRepository.findAll();
    }

    @Post
    Publisher<Genre> save(String name) {
        return genreRepository.create(name);
    }
}
