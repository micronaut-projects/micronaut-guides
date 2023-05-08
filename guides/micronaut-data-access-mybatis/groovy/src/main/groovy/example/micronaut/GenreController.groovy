package example.micronaut

import example.micronaut.domain.Genre
import example.micronaut.genre.GenreRepository
import example.micronaut.genre.GenreSaveCommand
import example.micronaut.genre.GenreUpdateCommand
import groovy.transform.CompileStatic
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put

import jakarta.validation.Valid

import static io.micronaut.http.HttpHeaders.LOCATION

@CompileStatic
@Controller('/genres') // <1>
class GenreController {

    private final GenreRepository genreRepository

    GenreController(GenreRepository genreRepository) { // <2>
        this.genreRepository = genreRepository
    }

    @Get('/{id}') // <3>
    Genre show(long id) {
        genreRepository.findById(id).orElse(null) // <4>
    }

    @Put // <5>
    HttpResponse<?> update(@Body @Valid GenreUpdateCommand command) { // <6>
        genreRepository.update(command.id, command.name)
        HttpResponse
                .noContent()
                .header(LOCATION, location(command.id).path) // <7>
    }

    @Get(value = '/list{?args*}') // <8>
    List<Genre> list(@Valid ListingArguments args) {
        genreRepository.findAll(args)
    }

    @Post // <9>
    HttpResponse<Genre> save(@Body @Valid GenreSaveCommand cmd) {
        Genre genre = genreRepository.save(cmd.name)

        HttpResponse
                .created(genre)
                .headers(headers -> headers.location(location(genre.id)))
    }

    @Delete('/{id}') // <10>
    HttpResponse<?> delete(Long id) {
        genreRepository.deleteById(id)
        HttpResponse.noContent()
    }

    private URI location(Long id) {
        URI.create('/genres/' + id)
    }
}
