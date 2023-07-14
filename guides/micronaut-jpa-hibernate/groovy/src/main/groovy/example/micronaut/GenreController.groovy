package example.micronaut

import example.micronaut.domain.Genre
import groovy.transform.CompileStatic
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

import static io.micronaut.http.HttpHeaders.LOCATION

@CompileStatic
@ExecuteOn(TaskExecutors.IO)  // <1>
@Controller('/genres')  // <2>
class GenreController {

    private final GenreRepository genreRepository

    GenreController(GenreRepository genreRepository) { // <3>
        this.genreRepository = genreRepository
    }

    @Get('/{id}') // <4>
    Genre show(Long id) {
        genreRepository
                .findById(id)
                .orElse(null) // <5>
    }

    @Put // <6>
    HttpResponse<?> update(@Body @Valid GenreUpdateCommand command) { // <7>
        int numberOfEntitiesUpdated = genreRepository.update(command.id, command.name)

        HttpResponse
                .noContent()
                .header(LOCATION, location(command.id).path) // <8>
    }

    @Get(value = '/list{?args*}') // <9>
    List<Genre> list(@Valid SortingAndOrderArguments args) {
        genreRepository.findAll(args)
    }

    @Post // <10>
    HttpResponse<Genre> save(@Body @Valid GenreSaveCommand cmd) {
        Genre genre = genreRepository.save(cmd.name)

        HttpResponse
                .created(genre)
                .headers(headers -> headers.location(location(genre.id)))
    }

    @Post('/ex') // <11>
    HttpResponse<Genre> saveExceptions(@Body @Valid GenreSaveCommand cmd) {
        try {
            Genre genre = genreRepository.saveWithException(cmd.name)
            HttpResponse
                    .created(genre)
                    .headers(headers -> headers.location(location(genre.id)))
        } catch(PersistenceException ignored) {
            HttpResponse.noContent()
        }
    }

    @Delete('/{id}') // <12>
    HttpResponse<?> delete(Long id) {
        genreRepository.deleteById(id)
        HttpResponse.noContent()
    }

    private URI location(Long id) {
        URI.create('/genres/' + id)
    }
}
