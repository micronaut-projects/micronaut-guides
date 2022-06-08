package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MutableHttpHeaders
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status
import reactor.core.publisher.Mono
import java.net.URI
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Controller("/genres") // <1>
class GenreController (private val genreRepository: GenreRepository) {     // <2>

    @Get("/{id}") // <3>
    fun show(id: Long): Mono<Genre> {
        return genreRepository
            .findById(id) // <4>
    }

    @Put // <5>
    fun update(@Body command: @Valid GenreUpdateCommand): Mono<HttpResponse<*>> { // <6>
        return genreRepository.update(command.id, command.name)
            .thenReturn(
                HttpResponse
                    .noContent<Any>()
                    .header(HttpHeaders.LOCATION, location(command.id).path)
            ) // <7>
    }

    @Get("/list") // <8>
    fun list(pageable: @Valid Pageable): Mono<List<Genre>> { // <9>
        return genreRepository.findAll(pageable)
            .map { it.content }
    }

    @Post // <10>
    fun save(@Body("name") name: @NotBlank String): Mono<HttpResponse<Genre>> {
        return genreRepository.save(name)
            .map { genre ->
                HttpResponse
                    .created(genre)
                    .headers { headers: MutableHttpHeaders ->
                        headers.location(
                            location(genre)
                        )
                    }
            }
    }

    @Post("/ex") // <11>
    fun saveExceptions(@Body name: @NotBlank String): Mono<MutableHttpResponse<Genre>> {
        return genreRepository.saveWithException(name)
            .map { genre ->
                HttpResponse
                    .created(genre)
                    .headers { headers: MutableHttpHeaders ->
                        headers.location(
                            location(genre)
                        )
                    }
            }.onErrorReturn(HttpResponse.noContent())
    }

    @Delete("/{id}") // <12>
    @Status(HttpStatus.NO_CONTENT)
    fun delete(id: Long): Mono<Void> {
        return genreRepository.deleteById(id)
            .then()
    }

    private fun location(id: Long?): URI {
        return URI.create("/genres/$id")
    }

    private fun location(genre: Genre): URI {
        return location(genre.id)
    }
}