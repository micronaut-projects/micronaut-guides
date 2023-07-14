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
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank

@Controller("/genres") // <1>
open class GenreController(private val genreRepository: GenreRepository) {     // <2>

    @Get("/{id}") // <3>
    fun show(id: Long): Mono<Genre> {
        return genreRepository
            .findById(id) // <4>
    }

    @Put // <5>
    open fun update(@Body @Valid command: GenreUpdateCommand): Mono<HttpResponse<*>> { // <6>
        return genreRepository.update(command.id, command.name)
            .thenReturn(
                HttpResponse
                    .noContent<Any>()
                    .header(HttpHeaders.LOCATION, location(command.id).path)
            ) // <7>
    }

    @Get("/list") // <8>
    open fun list(@Valid pageable: Pageable): Mono<List<Genre>> { // <9>
        return genreRepository.findAll(pageable)
            .map { it.content }
    }

    @Post // <10>
    open fun save(@NotBlank @Body("name") name: String): Mono<HttpResponse<Genre>> {
        return genreRepository.save(name)
            .map(this::createGenre)
    }

    @Post("/ex") // <11>
    open fun saveExceptions(@NotBlank @Body name: String): Mono<MutableHttpResponse<Genre>> {
        return genreRepository.saveWithException(name)
            .map(this::createGenre)
            .onErrorReturn(HttpResponse.noContent())
    }

    @Delete("/{id}") // <12>
    @Status(HttpStatus.NO_CONTENT)
    fun delete(id: Long): Mono<Void> {
        return genreRepository.deleteById(id)
            .then()
    }

    private fun createGenre(genre: Genre) : MutableHttpResponse<Genre> {
        return HttpResponse
            .created(genre)
            .headers { headers: MutableHttpHeaders ->
                headers.location(location(genre))
            }
    }

    private fun location(id: Long?): URI {
        return URI.create("/genres/$id")
    }

    private fun location(genre: Genre): URI {
        return location(genre.id)
    }
}