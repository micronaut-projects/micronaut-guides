package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import java.net.URI
import java.util.Optional
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

@ExecuteOn(TaskExecutors.IO) // <1>
@Controller("/genres") // <2>
open class GenreController(private val genreRepository: GenreRepository) { //<3>


    @Get("/{id}") //<4>
    fun show(id:Long): Optional<Genre> =
        genreRepository.findById(id) // <5>

    @Put // <6>
    open fun update(@Body @Valid command: GenreUpdateCommand): HttpResponse<Genre> { // <7>
        val id = genreRepository.update(command.id, command.name)

        return HttpResponse
            .noContent<Genre>()
            .header(HttpHeaders.LOCATION, id.location.path) // <8>
    }

    @Get("/list") // <9>
    open fun list(@Valid pageable: Pageable): List<Genre> = //<10>
        genreRepository.findAll(pageable).content


    @Post // <11>
    open fun save(@Body("name") @NotBlank name: String) : HttpResponse<Genre> {
        val genre = genreRepository.save(name)

        return HttpResponse
            .created(genre)
            .headers { headers -> headers.location(genre.location) }
    }

    @Post("/ex") // <12>
    open fun saveExceptions(@Body @NotBlank name: String): HttpResponse<Genre> {
        return try {
            val genre = genreRepository.saveWithException(name)

            HttpResponse
                .created(genre)
                .headers { headers -> headers.location(genre.location) }
        } catch (ex: DataAccessException) {
            HttpResponse.noContent()
        }
    }

    @Delete("/{id}") // <13>
    @Status(HttpStatus.NO_CONTENT)
    fun delete(id: Long) = genreRepository.deleteById(id)

    private val Long?.location : URI
        get() = URI.create("/genres/$this")

    private val Genre.location : URI
        get() = id.location
}