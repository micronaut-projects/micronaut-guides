package example.micronaut

import example.micronaut.domain.Genre
import io.micronaut.core.async.annotation.SingleResult
import io.micronaut.http.*
import io.micronaut.http.annotation.*
import org.reactivestreams.Publisher
import reactor.core.publisher.Mono
import java.net.URI
import jakarta.persistence.PersistenceException
import jakarta.validation.Valid

@Controller("/genres")  // <1>
open class GenreController(val genreRepository: GenreRepository) { // <2>

    @Get("/{id}") // <3>
    @SingleResult
    fun show(id: Long): Publisher<Genre?> {
        return genreRepository.findById(id) // <4>
    }

    @Put // <5>
    open fun update(@Valid @Body command: GenreUpdateCommand): Publisher<HttpResponse<*>> { // <6>// <6>
        return Mono.from(genreRepository.update(command.id, command.name))
            .map {
                HttpResponse
                    .noContent<Any>()
                    .header(HttpHeaders.LOCATION, location(command.id).path)
            } // <7>
    }

    private fun location(id: Long): URI {
        return URI.create("/genres/$id")
    }

    @Get(value = "/list{?args*}") // <8>
    open fun list(@Valid args: SortingAndOrderArguments): Publisher<Genre?> {
        return genreRepository.findAll(args)
    }

    @Post // <9>
    @SingleResult
    open fun save(@Valid @Body cmd: GenreSaveCommand): Publisher<HttpResponse<Genre>>? {
        return Mono.from(genreRepository.save(cmd.name))
            .map { genre ->
                HttpResponse
                    .created(genre)
                    .headers { headers ->
                        headers.location(location(genre.id!!))
                    }
            }
    }

    @Post("/ex") // <10>
    @SingleResult
    open fun saveExceptions(@Valid @Body cmd: GenreSaveCommand): Publisher<MutableHttpResponse<Genre?>> {
        return Mono.from(genreRepository.saveWithException(cmd.name))
            .map { genre: Genre? ->
                HttpResponse
                    .created(genre)
                    .headers { headers: MutableHttpHeaders ->
                        headers.location(
                            location(
                                genre!!.id!!
                            )
                        )
                    }
            }
            .onErrorReturn(PersistenceException::class.java, HttpResponse.noContent())
    }

    @Delete("/{id}") // <11>
    @Status(HttpStatus.NO_CONTENT) // <12>
    fun delete(id: Long): HttpResponse<*> {
        genreRepository.deleteById(id)
        return HttpResponse.noContent<Any>()
    }
}