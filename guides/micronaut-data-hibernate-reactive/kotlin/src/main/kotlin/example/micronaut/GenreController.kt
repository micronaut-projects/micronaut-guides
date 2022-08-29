package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.data.exceptions.DataAccessException
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.MutableHttpHeaders
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.uri.UriBuilder
import reactor.core.publisher.Mono
import java.net.URI
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Controller("/genres")  // <1>
open class GenreController(private val genreRepository: GenreRepository) { // <2>

    @Get("/{id}") // <3>
    fun show(id: Long): Mono<Genre?> {
        return genreRepository.findById(id) // <4>
    }

    @Put // <5>
    open fun update(@Valid @Body command: GenreUpdateCommand): Mono<HttpResponse<Genre>> { // <6>
        return genreRepository.update(command.id, command.name)
            .map {
                HttpResponse
                    .noContent<Genre>()
                    .header(HttpHeaders.LOCATION, location(command.id).path)
            } // <7>
    }

    @Get("/list") // <8>
    open fun list(@Valid pageable: Pageable): Mono<List<Genre?>> { // <9>
        return genreRepository.findAll(pageable)
            .map { obj -> obj.content }
    }

    @Post // <10>
    open fun save(@NotBlank @Body("name") name: String): Mono<HttpResponse<Genre>> {
        return genreRepository.save(name)
            .map {
                HttpResponse.created(it!!)
                    .headers { headers: MutableHttpHeaders ->
                        headers.location(location(it.id!!))
                    }
            }
    }

    @Post("/ex") // <11>
    open fun saveExceptions(@NotBlank @Body name: String): Mono<MutableHttpResponse<Genre?>> {
        return genreRepository
            .saveWithException(name)
            .map { genre ->
                HttpResponse
                    .created(genre)
                    .headers { headers ->
                        headers.location(location(genre?.id!!))
                    }
            }
            .onErrorReturn(DataAccessException::class.java, HttpResponse.noContent())
    }

    @Delete("/{id}") // <12>
    open fun delete(id: Long): Mono<HttpResponse<*>>? {
        return genreRepository.deleteById(id)
            .map { HttpResponse.noContent<Any>() }
    }

    private fun location(id: Long): URI {
        return UriBuilder.of("/genres").path(id.toString()).build()
    }
}